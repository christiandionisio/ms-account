package com.example.msaccount.service;

import com.example.msaccount.models.AccountConfiguration;
import com.example.msaccount.repo.AccountConfigurationRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class AccountConfigurationServiceImplTest {

    @MockBean
    private AccountConfigurationRepository repository;

    @Autowired
    private AccountConfigurationServiceImpl service;

    @Test
    void findAllTest() {
        List<AccountConfiguration> accountConfigurationList = new ArrayList<>();
        accountConfigurationList.add(new AccountConfiguration());

        Mockito.when(repository.findAll())
                .thenReturn(Flux.fromIterable(accountConfigurationList));

        Flux<AccountConfiguration> accountConfigurationFlux = service.findAll();

        StepVerifier.create(accountConfigurationFlux)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void findByIdTest() {
        Mockito.when(repository.findById("1"))
                .thenReturn(Mono.just(new AccountConfiguration()));

        Mono<AccountConfiguration> accountConfigurationMono = service.findById("1");

        StepVerifier.create(accountConfigurationMono)
                .expectNext(new AccountConfiguration())
                .verifyComplete();
    }

    @Test
    void createTest() {
        Mockito.when(repository.save(new AccountConfiguration()))
                .thenReturn(Mono.just(new AccountConfiguration()));

        Mono<AccountConfiguration> accountConfigurationMono = service.create(new AccountConfiguration());

        StepVerifier.create(accountConfigurationMono)
                .expectNext(new AccountConfiguration())
                .verifyComplete();
    }

    @Test
    void updateTest() {
        Mockito.when(repository.save(new AccountConfiguration()))
                .thenReturn(Mono.just(new AccountConfiguration()));

        Mono<AccountConfiguration> accountConfigurationMono = service.update(new AccountConfiguration());

        StepVerifier.create(accountConfigurationMono)
                .expectNext(new AccountConfiguration())
                .verifyComplete();
    }

    @Test
    void deleteTest() {
        Mockito.when(repository.deleteById("1"))
                .thenReturn(Mono.empty());

        Mono<Void> voidMono = service.delete("1");

        StepVerifier.create(voidMono)
                .verifyComplete();
    }

    @Test
    void findByAccountTypeAndNameTest() {
        Mockito.when(repository.findByAccountTypeAndName(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Mono.just(new AccountConfiguration()));

        Mono<AccountConfiguration> accountConfigurationMono = service.findByAccountTypeAndName("1", "1");

        StepVerifier.create(accountConfigurationMono)
                .expectNext(new AccountConfiguration())
                .verifyComplete();
    }

}