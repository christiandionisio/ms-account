package com.example.msaccount.service;

import com.example.msaccount.dto.AccountCustomerDTO;
import com.example.msaccount.models.Account;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IAccountService {
    Flux<Account> findAll();
    Mono<Account> findById(String id);
    Mono<Account> create(AccountCustomerDTO account);
    Mono<Account> update(Account account);
    void delete(String id);
    Flux<Account> findByHoldersId(String holdersId);
}
