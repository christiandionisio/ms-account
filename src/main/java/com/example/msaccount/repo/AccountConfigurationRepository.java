package com.example.msaccount.repo;

import com.example.msaccount.models.AccountConfiguration;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface AccountConfigurationRepository extends ReactiveMongoRepository<AccountConfiguration, String> {
    Mono<AccountConfiguration> findByAccountTypeAndName(String accountType, String name);
}
