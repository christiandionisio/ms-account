package com.example.msaccount.service;

import com.example.msaccount.models.Account;
import com.example.msaccount.repo.AccountRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
class AccountServiceImplTest {

    @MockBean
    private AccountRepository repository;

    @Autowired
    private AccountServiceImpl accountService;

    @Test
    void findAll() {
        Flux<Account> accounts = Flux.just(new Account(), new Account());
        Mockito.when(repository.findAll()).thenReturn(accounts);
        StepVerifier.create(accountService.findAll())
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    void findById() {
        Mockito.when(repository.findById(Mockito.anyString()))
                .thenReturn(Mono.just(new Account()));

        Mono<Account> response = accountService.findById("1");

        StepVerifier.create(response)
                .expectNextCount(1)
                .verifyComplete();
    }

}