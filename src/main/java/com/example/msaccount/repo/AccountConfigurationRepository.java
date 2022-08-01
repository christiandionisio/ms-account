package com.example.msaccount.repo;

import com.example.msaccount.models.AccountConfiguration;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface AccountConfigurationRepository extends ReactiveMongoRepository<AccountConfiguration, String> {

}
