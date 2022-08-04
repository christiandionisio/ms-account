package com.example.msaccount.service;

import com.example.msaccount.models.CustomerAccount;
import com.example.msaccount.repo.CustomerAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CustomerAccountServiceImpl implements ICustomerAccountService {

  @Autowired
  private CustomerAccountRepository repository;

  @Override
  public Mono<CustomerAccount> save(CustomerAccount customerAccount) {
    return repository.save(customerAccount);
  }

  @Override
  public Flux<CustomerAccount> findByIdCustomer(String idCustomer) {
    return repository.findByIdCustomer(idCustomer);
  }
}
