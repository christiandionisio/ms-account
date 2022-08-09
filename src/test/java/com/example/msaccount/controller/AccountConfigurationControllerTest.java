package com.example.msaccount.controller;

import com.example.msaccount.dto.AccountConfigurationDto;
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

    @Test
    @DisplayName("Create configuration")
    void create() {
        AccountConfigurationDto accountConfiguration = new AccountConfigurationDto();
        Mockito.when(service.create(Mockito.any(AccountConfiguration.class)))
                .thenReturn(Mono.just(getAccountConfigurationTest()));

        webClient.post().uri("/accounts/configurations")
                .body(Mono.just(accountConfiguration), AccountConfigurationDto.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(AccountConfiguration.class)
                .isEqualTo(getAccountConfigurationTest());
    }

    @Test
    @DisplayName("Update configuration")
    void update() {
        AccountConfigurationDto accountConfiguration = new AccountConfigurationDto();
        Mockito.when(service.update(Mockito.any(AccountConfiguration.class)))
                .thenReturn(Mono.just(getAccountConfigurationTest()));

        webClient.put().uri("/accounts/configurations")
                .body(Mono.just(accountConfiguration), AccountConfigurationDto.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(AccountConfiguration.class)
                .isEqualTo(getAccountConfigurationTest());
    }

    @Test
    @DisplayName("Delete configuration")
    void delete() {
        Mockito.when(service.delete(Mockito.anyString()))
                .thenReturn(Mono.empty());

        webClient.delete().uri("/accounts/configurations/1")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @DisplayName("Find configuration by account type and name")
    void findByAccountTypeAndName() {
        AccountConfiguration accountConfiguration = getAccountConfigurationTest();
        Mockito.when(service.findByAccountTypeAndName(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Mono.just(accountConfiguration));

        webClient.get().uri("/accounts/configurations/searchByAccountTypeAndName?accountType=1&name=1")
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