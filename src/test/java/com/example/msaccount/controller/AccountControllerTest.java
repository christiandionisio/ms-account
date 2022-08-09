package com.example.msaccount.controller;

import com.example.msaccount.dto.AccountCustomerDto;
import com.example.msaccount.dto.AccountDto;
import com.example.msaccount.error.AccountCustomerWithoutCreditCardRequiredException;
import com.example.msaccount.error.AccountInvalidBalanceException;
import com.example.msaccount.error.AccountToBusinessCustomerNotAllowedExecption;
import com.example.msaccount.error.PersonalCustomerHasAccountException;
import com.example.msaccount.models.Account;
import com.example.msaccount.service.IAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AccountControllerTest {

    @MockBean
    IAccountService accountService;

    @Autowired
    private WebTestClient webClient;

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("Find all accounts")
    void findAll() {
        List<Account> accountsMock = new ArrayList<>();
        accountsMock.add(getAccountTest());

        Mockito.when(accountService.findAll())
                .thenReturn(Flux.fromIterable(accountsMock));

        webClient.get().uri("/accounts")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Account.class)
                .hasSize(1);
    }

    @Test
    @DisplayName("Create account")
    void create() {
        Mockito.when(accountService.create(Mockito.any(AccountCustomerDto.class)))
                .thenReturn(Mono.just(getAccountTest()));

        webClient.post().uri("/accounts")
                .body(Mono.just(new AccountCustomerDto()), AccountCustomerDto.class)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @DisplayName("Create account with PersonalCustomerHasAccountException")
    void createWithPersonalCustomerHasAccountException() {
        Mockito.when(accountService.create(Mockito.any(AccountCustomerDto.class)))
                .thenReturn(Mono.error(new PersonalCustomerHasAccountException()));

        webClient.post().uri("/accounts")
                .body(Mono.just(new AccountCustomerDto()), AccountCustomerDto.class)
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    @DisplayName("Create account with AccountToBusinessCustomerNotAllowedExecption")
    void createWithAccountToBusinessCustomerNotAllowedExecption() {
        Mockito.when(accountService.create(Mockito.any(AccountCustomerDto.class)))
                .thenReturn(Mono.error(new AccountToBusinessCustomerNotAllowedExecption()));

        webClient.post().uri("/accounts")
                .body(Mono.just(new AccountCustomerDto()), AccountCustomerDto.class)
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    @DisplayName("Create account with AccountInvalidBalanceException")
    void createWithAccountInvalidBalanceException() {
        Mockito.when(accountService.create(Mockito.any(AccountCustomerDto.class)))
                .thenReturn(Mono.error(new AccountInvalidBalanceException()));

        webClient.post().uri("/accounts")
                .body(Mono.just(new AccountCustomerDto()), AccountCustomerDto.class)
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    @DisplayName("Create account with AccountCustomerWithoutCreditCardRequiredException")
    void createWithAccountCustomerWithoutCreditCardRequiredException() {
        Mockito.when(accountService.create(Mockito.any(AccountCustomerDto.class)))
                .thenReturn(Mono.error(new AccountCustomerWithoutCreditCardRequiredException("TEST")));

        webClient.post().uri("/accounts")
                .body(Mono.just(new AccountCustomerDto()), AccountCustomerDto.class)
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    @DisplayName("Create account with GeneralException")
    void createWithGeneralException() {
        Mockito.when(accountService.create(Mockito.any(AccountCustomerDto.class)))
                .thenReturn(Mono.error(new Exception("GeneralException TEST")));

        webClient.post().uri("/accounts")
                .body(Mono.just(new AccountCustomerDto()), AccountCustomerDto.class)
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    @DisplayName("Update account")
    void update() {
        Mockito.when(accountService.findById(Mockito.anyString()))
                .thenReturn(Mono.just(getAccountTest()));
        Mockito.when(accountService.update(Mockito.any(Account.class)))
                .thenReturn(Mono.just(getAccountTest()));

        AccountDto accountDto = new AccountDto();
        accountDto.setAccountId("1");

        webClient.put().uri("/accounts")
                .body(Mono.just(accountDto), AccountDto.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Account.class)
                .isEqualTo(getAccountTest());
    }

    @Test
    @DisplayName("Delete account")
    void delete() {
        Mockito.when(accountService.findById(Mockito.anyString()))
                .thenReturn(Mono.just(getAccountTest()));
        Mockito.when(accountService.delete(Mockito.anyString()))
                .thenReturn(Mono.empty());

        webClient.delete().uri("/accounts/1")
                .exchange()
                .expectStatus().isNoContent();
    }

    private Account getAccountTest() {
        Account account = new Account();
        account.setAccountId("1");
        account.setAccountNumber("123456");
        account.setAccountType("CORRIENTE");
        account.setBalance(BigDecimal.valueOf(1000));
        account.setCurrency("USD");
        account.setCustomerOwnerId("123");
        account.setCustomerOwnerType("PERSONAL");
        return account;
    }
}