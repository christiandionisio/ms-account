package com.example.msaccount.service;

import com.example.msaccount.models.CustomerAccount;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ICustomerAccountService {
    Mono<CustomerAccount> save(CustomerAccount customerAccount);
    Flux<CustomerAccount> findByIdCustomer(String idCustomer);
}
