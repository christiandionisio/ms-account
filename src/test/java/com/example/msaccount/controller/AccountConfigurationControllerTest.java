package com.example.msaccount.controller;

import com.example.msaccount.models.AccountConfiguration;
import com.example.msaccount.service.IAccountConfigurationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AccountConfigurationControllerTest {

    @MockBean
    private IAccountConfigurationService service;

    @Autowired
    private WebTestClient webClient;

    @Test
    @DisplayName("Find all configurations")
    void findAll() {
        List<AccountConfiguration> accountConfigurationList = new ArrayList<>();
        accountConfigurationList.add(getAccountConfigurationTest());

        Mockito.when(service.findAll())
                .thenReturn(Flux.fromIterable(accountConfigurationList));

        webClient.get().uri("/accounts/configurations")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(AccountConfiguration.class)
                .hasSize(1);
    }

    @Test
    @DisplayName("Find configuration by id")
    void findById() {
        AccountConfiguration accountConfiguration = getAccountConfigurationTest();
        Mockito.when(service.findById(Mockito.anyString()))
                .thenReturn(Mono.just(accountConfiguration));

        webClient.get().uri("/accounts/configurations/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(AccountConfiguration.class)
                .isEqualTo(accountConfiguration);
    }


    private AccountConfiguration getAccountConfigurationTest() {
        AccountConfiguration accountConfiguration = new AccountConfiguration();
        accountConfiguration.setId("1");
        accountConfiguration.setAccountType("CORRIENTE");
        accountConfiguration.setName("TEST CONFIG");
        accountConfiguration.setValue(200);
        return accountConfiguration;
    }

}