package com.example.msaccount.repo;

import com.example.msaccount.models.CustomerAccount;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface CustomerAccountRepository extends ReactiveMongoRepository<CustomerAccount, String> {
  Flux<CustomerAccount> findByIdCustomer(String idCustomer);
}
