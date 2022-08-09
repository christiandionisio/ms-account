package com.example.msaccount.controller;

import com.example.msaccount.dto.CustomerAccountDto;
import com.example.msaccount.models.CustomerAccount;
import com.example.msaccount.service.ICustomerAccountService;
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
class CustomerAccountControllerTest {

    @MockBean
    private ICustomerAccountService service;

    @Autowired
    private WebTestClient webClient;

    @Test
    @DisplayName("Create configuration")
    void create() {
        CustomerAccountDto customerAccountDto = new CustomerAccountDto();
        Mockito.when(service.save(Mockito.any(CustomerAccount.class)))
                .thenReturn(Mono.just(getCustomerAccountTest()));

        webClient.post().uri("/customer-accounts")
                .body(Mono.just(customerAccountDto), CustomerAccountDto.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(CustomerAccount.class)
                .isEqualTo(getCustomerAccountTest());
    }

    @Test
    @DisplayName("Find by customer id")
    void findByCustomerId() {
        List<CustomerAccount> customerAccounts = new ArrayList<>();
        customerAccounts.add(getCustomerAccountTest());

        Mockito.when(service.findByIdCustomer(Mockito.anyString()))
                .thenReturn(Flux.fromIterable(customerAccounts));

        webClient.get().uri("/customer-accounts/{idCustomer}", "1")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CustomerAccount.class);
    }

    private CustomerAccount getCustomerAccountTest() {
        CustomerAccount customerAccount = new CustomerAccount();
        customerAccount.setId("1");
        customerAccount.setIdCustomer("1");
        customerAccount.setIdAccount("1");

        return customerAccount;
    }

}