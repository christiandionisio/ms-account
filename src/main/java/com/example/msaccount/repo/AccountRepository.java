package com.example.msaccount.repo;

import com.example.msaccount.models.Account;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface AccountRepository extends ReactiveMongoRepository<Account, String> {
    Mono<Account> findByAccountIdAndAccountType(String accountId, String accountType);
}
