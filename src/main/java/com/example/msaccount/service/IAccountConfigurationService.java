package com.example.msaccount.service;

import com.example.msaccount.models.AccountConfiguration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IAccountConfigurationService {

    Flux<AccountConfiguration> findAll();
    Mono<AccountConfiguration> findById(String id);
    Mono<AccountConfiguration> create(AccountConfiguration account);
    Mono<AccountConfiguration> update(AccountConfiguration account);
    Mono<Void> delete(String id);

}
