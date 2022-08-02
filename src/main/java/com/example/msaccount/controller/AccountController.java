package com.example.msaccount.controller;

import com.example.msaccount.dto.AccountCustomerDTO;
import com.example.msaccount.dto.HolderAccountDTO;
import com.example.msaccount.dto.ResponseTemplateDTO;
import com.example.msaccount.error.*;
import com.example.msaccount.models.Account;
import com.example.msaccount.models.CustomerAccount;
import com.example.msaccount.service.IAccountService;
import com.example.msaccount.service.ICustomerAccountService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    private IAccountService service;

    @Autowired ICustomerAccountService customerAccountService;

    private static final Logger logger = LogManager.getLogger(AccountController.class);

    @GetMapping()
    public Mono<ResponseEntity<Flux<Account>>> findAll() {
        return Mono.just(new ResponseEntity<>(service.findAll(), HttpStatus.OK));
    }

    @PostMapping()
    public Mono<ResponseEntity<Object>> create(@RequestBody AccountCustomerDTO accountCustomerDTO) {
        return service.create(accountCustomerDTO)
                .flatMap(accountCreated -> customerAccountService
                        .save(new CustomerAccount(null, accountCustomerDTO.getHolder(), accountCreated.getAccountId()))
                        .flatMap(response -> Mono.just(new ResponseEntity<>(HttpStatus.OK)))
                )
                .onErrorResume(e -> {
                    if (e instanceof PersonalCustomerHasAccountException ||
                        e instanceof AccountToBusinessCustomerNotAllowedExecption ||
                            e instanceof AccountInvalidBalanceException ||
                        e instanceof AccountCustomerWithoutCreditCardRequiredException) {
                        logger.error(e.getMessage());
                        return Mono.just(new ResponseEntity<>(new ResponseTemplateDTO(null,
                                e.getMessage()), HttpStatus.FORBIDDEN));
                    }
                    logger.error(e.getMessage());
                    return Mono.just(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
                })
                .defaultIfEmpty(new ResponseEntity<>(new ResponseTemplateDTO(null,
                        "Customer not found"), HttpStatus.NOT_FOUND));
    }

    @PutMapping
    public Mono<ResponseEntity<Mono<Account>>> update(@RequestBody Account account) {
        return service.findById(account.getAccountId())
                .flatMap(accountFound ->
                        Mono.just(new ResponseEntity<>(service.update(account), HttpStatus.OK)))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Mono<Void>>> delete(@PathVariable String id) {
        return service.findById(id)
                .flatMap(account -> Mono.just(new ResponseEntity<>(service.delete(id), HttpStatus.NO_CONTENT)))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("findByHoldersId/{id}")
    public Flux<Account> findByHoldersId(@PathVariable String holdersId) {
        return service.findByHoldersId(holdersId);
    }

    @PostMapping("/addHolder")
    public Mono<ResponseEntity<ResponseTemplateDTO>> addHolderInAccount(@RequestBody HolderAccountDTO holderAccountDTO) {
        return service.addHolders(holderAccountDTO.getHolderId(), holderAccountDTO.getAccountId())
                .flatMap(response -> Mono.just(ResponseEntity.ok().body(new ResponseTemplateDTO(response, ""))))
                .onErrorResume(e -> {
                    if (e instanceof AccountNotSupportedForMultipleHoldersException ||
                            e instanceof HolderAlredyExistInAccountEException) {
                        logger.error(e.getMessage());
                        return Mono.just(new ResponseEntity<>(new ResponseTemplateDTO(null,
                                e.getMessage()), HttpStatus.FORBIDDEN));
                    }
                    logger.error(e.getMessage());
                    return Mono.just(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
                });
    }
    @GetMapping("/{id}")
    public Mono<Account> read(@PathVariable String id){
        Mono<Account> account = service.findById(id);
        return account;
    }

    @GetMapping("/balance/{accountId}")
    public Mono<ResponseEntity<Object>> getBalanceAvailable(@PathVariable String accountId){
        return service.getBalance(accountId)
                .flatMap(balance -> {
                    ResponseEntity<Object> response = ResponseEntity.ok().body(balance);
                    return Mono.just(response);
                })
                .defaultIfEmpty(new ResponseEntity<>(new ResponseTemplateDTO(HttpStatus.NOT_FOUND,
                        "Account not found"), HttpStatus.NOT_FOUND));
    }
}
