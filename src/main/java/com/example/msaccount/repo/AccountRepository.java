package com.example.msaccount.repo;

import com.example.msaccount.models.Account;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface AccountRepository extends ReactiveMongoRepository<Account, String> {

}
