package com.example.msaccount.controller;

import com.example.msaccount.dto.AccountCustomerDTO;
import com.example.msaccount.dto.HolderAccountDTO;
import com.example.msaccount.dto.ResponseTemplateDTO;
import com.example.msaccount.error.AccountNotSupportedForMultipleHoldersException;
import com.example.msaccount.error.AccountToBusinessCustomerNotAllowedExecption;
import com.example.msaccount.error.HolderAlredyExistInAccountEException;
import com.example.msaccount.error.PersonalCustomerHasAccountException;
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
    public Flux<Account> findAll() {
        logger.debug("Debugging log");
        logger.info("Info log");
        logger.warn("Hey, This is a warning!");
        logger.error("Oops! We have an Error. OK");
        logger.fatal("Damn! Fatal error. Please fix me.");
        return service.findAll();
    }

    @PostMapping()
    public Mono<ResponseEntity<Object>> create(@RequestBody AccountCustomerDTO accountCustomerDTO) {
        return service.create(accountCustomerDTO)
                .flatMap(accountCreated -> customerAccountService
                        .save(new CustomerAccount(null, accountCustomerDTO.getHolder(), accountCreated.getAccountId()))
                        .flatMap(response -> Mono.just(new ResponseEntity<>(HttpStatus.NO_CONTENT)))
                )
                .onErrorResume(e -> {
                    if (e instanceof PersonalCustomerHasAccountException ||
                        e instanceof AccountToBusinessCustomerNotAllowedExecption) {
                        return Mono.just(new ResponseEntity<>(new ResponseTemplateDTO(null,
                                e.getMessage()), HttpStatus.FORBIDDEN));
                    }
                    return Mono.just(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
                })
                .defaultIfEmpty(new ResponseEntity<>(new ResponseTemplateDTO(null,
                        "Customer not found"), HttpStatus.NOT_FOUND));
    }

    @PutMapping
    public Mono<Account> update(@RequestBody Account account) {
        return service.update(account);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.delete(id);
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
                        return Mono.just(new ResponseEntity<>(new ResponseTemplateDTO(null,
                                e.getMessage()), HttpStatus.FORBIDDEN));
                    }
                    return Mono.just(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
                });
    }
    @GetMapping("/{id}")
    public Mono<Account> read(@PathVariable String id){
        Mono<Account> account = service.findById(id);
        return account;
    }
}
