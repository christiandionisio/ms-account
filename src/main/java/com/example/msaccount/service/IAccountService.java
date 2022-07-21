package com.example.msaccount.service;

import com.example.msaccount.models.Account;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IAccountService {
    Flux<Account> findAll();
    Mono<Account> findById(String id);
    Mono<Account> create(Account account);
    Mono<Account> update(Account account);
    void delete(String id);
}
