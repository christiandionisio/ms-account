package com.example.msaccount.controller;

import com.example.msaccount.dto.*;
import com.example.msaccount.error.AccountCustomerWithoutCreditCardRequiredException;
import com.example.msaccount.error.AccountInvalidBalanceException;
import com.example.msaccount.error.AccountNotSupportedForMultipleHoldersException;
import com.example.msaccount.error.AccountToBusinessCustomerNotAllowedExecption;
import com.example.msaccount.error.CustomerHasCreditCardDebtException;
import com.example.msaccount.error.CustomerHasCreditDebtException;
import com.example.msaccount.error.HolderAlredyExistInAccountEException;
import com.example.msaccount.error.PersonalCustomerHasAccountException;
import com.example.msaccount.models.Account;
import com.example.msaccount.models.CustomerAccount;
import com.example.msaccount.service.IAccountService;
import com.example.msaccount.service.ICustomerAccountService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/accounts")
public class AccountController {

  @Autowired
  private IAccountService service;

  @Autowired
  ICustomerAccountService customerAccountService;

  private static final Logger logger = LogManager.getLogger(AccountController.class);

  private ModelMapper modelMapper = new ModelMapper();

  @GetMapping()
  public Mono<ResponseEntity<Flux<Account>>> findAll() {
    return Mono.just(new ResponseEntity<>(service.findAll(), HttpStatus.OK));
  }

  @PostMapping()
  public Mono<ResponseEntity<Object>> create(@RequestBody AccountCustomerDto accountCustomerDto) {
    return service.create(accountCustomerDto)
            .flatMap(accountCreated -> customerAccountService
                    .save(new CustomerAccount(null, accountCustomerDto.getHolder(), accountCreated.getAccountId()))
                    .flatMap(response -> Mono.just(new ResponseEntity<>(HttpStatus.OK)))
            )
            .onErrorResume(e -> {
              if (e instanceof PersonalCustomerHasAccountException
                      || e instanceof AccountToBusinessCustomerNotAllowedExecption
                      || e instanceof AccountInvalidBalanceException
                      || e instanceof AccountCustomerWithoutCreditCardRequiredException
                      || e instanceof CustomerHasCreditDebtException
                      || e instanceof CustomerHasCreditCardDebtException) {
                logger.error(e.getMessage());
                return Mono.just(new ResponseEntity<>(new ResponseTemplateDto(null,
                        e.getMessage()), HttpStatus.FORBIDDEN));
              }
              logger.error(e.getMessage());
              return Mono.just(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
            })
            .defaultIfEmpty(new ResponseEntity<>(new ResponseTemplateDto(null,
                    "Customer not found"), HttpStatus.NOT_FOUND));
  }

  @PutMapping
  public Mono<ResponseEntity<Mono<Account>>> update(@RequestBody AccountDto accountDto) {
    modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    return service.findById(accountDto.getAccountId())
            .flatMap(accountFound ->
                    Mono.just(new ResponseEntity<>(service.update(modelMapper.map(accountDto, Account.class)), HttpStatus.OK)))
            .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @DeleteMapping("/{id}")
  public Mono<ResponseEntity<Mono<Void>>> delete(@PathVariable String id) {
    return service.findById(id)
            .flatMap(account -> Mono.just(new ResponseEntity<>(service.delete(id), HttpStatus.NO_CONTENT)))
            .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @GetMapping("findByHoldersId/{holdersId}")
  public Flux<Account> findByHoldersId(@PathVariable String holdersId) {
    return service.findByHoldersId(holdersId);
  }

  @PostMapping("/addHolder")
  public Mono<ResponseEntity<ResponseTemplateDto>> addHolderInAccount(@RequestBody HolderAccountDto holderAccountDto) {
    return service.addHolders(holderAccountDto.getHolderId(), holderAccountDto.getAccountId())
            .flatMap(response -> Mono.just(ResponseEntity.ok().body(new ResponseTemplateDto(response, ""))))
            .onErrorResume(e -> {
              if (e instanceof AccountNotSupportedForMultipleHoldersException
                      || e instanceof HolderAlredyExistInAccountEException) {
                logger.error(e.getMessage());
                return Mono.just(new ResponseEntity<>(new ResponseTemplateDto(null,
                        e.getMessage()), HttpStatus.FORBIDDEN));
              }
              logger.error(e.getMessage());
              return Mono.just(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
            });
  }

  @GetMapping("/{id}")
  public Mono<Account> read(@PathVariable String id) {
    return service.findById(id);
  }

  @GetMapping("/balance/{accountId}")
  public Mono<ResponseEntity<Object>> getBalanceAvailable(@PathVariable String accountId) {
    return service.getBalance(accountId)
            .flatMap(balance -> {
              ResponseEntity<Object> response = ResponseEntity.ok().body(balance);
              return Mono.just(response);
            })
            .defaultIfEmpty(new ResponseEntity<>(new ResponseTemplateDto(HttpStatus.NOT_FOUND,
                    "Account not found"), HttpStatus.NOT_FOUND));
  }

  @GetMapping("/findByCustomerOwnerId/{customerOwnerId}")
  public Mono<ResponseEntity<Flux<Account>>> findByCustomerOwnerId(@PathVariable String customerOwnerId) {
    return Mono.just(new ResponseEntity<>(service.findByCustomerOwnerId(customerOwnerId), HttpStatus.OK));
  }

  @GetMapping("/findByCustomerOwnerIdAndAccountId")
  public Mono<ResponseEntity<Mono<Account>>> findByCustomerOwnerIdAndAccountId(@RequestParam String customerOwnerId, @RequestParam String accountId) {
    return Mono.just(new ResponseEntity<>(service.findByCustomerOwnerIdAndAccountId(customerOwnerId, accountId), HttpStatus.OK));
  }

  @GetMapping("/findByCustomerOwnerIdAndCardId")
  public Mono<ResponseEntity<Flux<Account>>> findByCustomerOwnerIdAndCardId(@RequestParam String customerOwnerId, @RequestParam String cardId) {
    return Mono.just(new ResponseEntity<>(service.findByCustomerOwnerIdAndCardId(customerOwnerId, cardId), HttpStatus.OK));
  }

  @GetMapping("/findByWalletPhoneNumber/{walletPhoneNumber}")
  public Mono<ResponseEntity<Mono<Account>>> findByWalletPhoneNumber(@PathVariable String walletPhoneNumber) {
      return Mono.just(new ResponseEntity<>(service.findByWalletPhoneNumber(walletPhoneNumber), HttpStatus.OK));
  }

  @PutMapping("/assingPhoneNumber/{accountId}")
    public Mono<ResponseEntity<Object>> assingPhoneNumber(@PathVariable String accountId,
                                                                 @RequestBody AssingPhoneNumberDto phoneNumberDto) {
        return service.findById(accountId)
                .flatMap(account -> {
                    account.setWalletPhoneNumber(phoneNumberDto.getPhoneNumber());
                    return service.update(account);
                }).flatMap(account -> Mono.just(new ResponseEntity<>(account, HttpStatus.OK)));
    }
}
