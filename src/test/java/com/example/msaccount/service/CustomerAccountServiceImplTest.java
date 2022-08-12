package com.example.msaccount.service;

import com.example.msaccount.models.CustomerAccount;
import com.example.msaccount.repo.CustomerAccountRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
class CustomerAccountServiceImplTest {

  @MockBean
  private CustomerAccountRepository repository;

  @Autowired
  private CustomerAccountServiceImpl service;

  @Test
  void findByIdCustomer() {
    Mockito.when(repository.findByIdCustomer(Mockito.anyString()))
            .thenReturn(Flux.just(new CustomerAccount()));

    Flux<CustomerAccount> response = service.findByIdCustomer("1");

    StepVerifier.create(response)
            .expectNextCount(1)
            .verifyComplete();
  }

  @Test
  void save() {
    Mockito.when(repository.save(Mockito.any(CustomerAccount.class)))
            .thenReturn(Mono.just(new CustomerAccount()));

    Mono<CustomerAccount> response = service.save(new CustomerAccount());

    StepVerifier.create(response)
            .expectNext(new CustomerAccount())
            .verifyComplete();
  }

}