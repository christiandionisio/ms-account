package com.example.msaccount.controller;

import com.example.msaccount.dto.*;
import com.example.msaccount.error.*;
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

    @Test
    @DisplayName("Find by holder ID")
    void findByHolderId() {
        List<Account> accountsMock = new ArrayList<>();
        accountsMock.add(getAccountTest());

        Mockito.when(accountService.findByHoldersId(Mockito.anyString()))
                .thenReturn(Flux.fromIterable(accountsMock));

        webClient.get().uri("/accounts/findByHoldersId/abc1")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Account.class)
                .hasSize(1);
    }

    @Test
    @DisplayName("Add holder to account")
    void addHolderInAccount() {
        Mockito.when(accountService.addHolders(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Mono.just(getAccountWithHoldersTest()));

        webClient.post().uri("/accounts/addHolder")
                .body(Mono.just(new HolderAccountDto("1", "2")), HolderAccountDto.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ResponseTemplateDto.class);
    }

    @Test
    @DisplayName("Add holder to account with AccountNotSupportedForMultipleHoldersException")
    void addHolderInAccountWithAccountNotSupportedForMultipleHoldersException() {
        Mockito.when(accountService.addHolders(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Mono.error(new AccountNotSupportedForMultipleHoldersException()));

        webClient.post().uri("/accounts/addHolder")
                .body(Mono.just(new HolderAccountDto("1", "2")), HolderAccountDto.class)
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    @DisplayName("Add holder to account with HolderAlredyExistInAccountEException")
    void addHolderInAccountWithHolderAlredyExistInAccountEException() {
        Mockito.when(accountService.addHolders(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Mono.error(new HolderAlredyExistInAccountEException()));

        webClient.post().uri("/accounts/addHolder")
                .body(Mono.just(new HolderAccountDto("1", "2")), HolderAccountDto.class)
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    @DisplayName("Add holder to account with GeneralException")
    void addHolderInAccountWithGeneralException() {
        Mockito.when(accountService.addHolders(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Mono.error(new Exception()));

        webClient.post().uri("/accounts/addHolder")
                .body(Mono.just(new HolderAccountDto("1", "2")), HolderAccountDto.class)
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    @DisplayName("Read account")
    void read() {
        Mockito.when(accountService.findById(Mockito.anyString()))
                .thenReturn(Mono.just(getAccountTest()));

        webClient.get().uri("/accounts/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Account.class)
                .isEqualTo(getAccountTest());
    }

    @Test
    @DisplayName("Get balance available service")
    void getBalanceAvailable() {
        Mockito.when(accountService.getBalance(Mockito.anyString()))
                .thenReturn(Mono.just(new BalanceDto(BigDecimal.valueOf(100), "PEN")));

        webClient.get().uri("/accounts/balance/1")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @DisplayName("Find by customer owner")
    void findByCustomerOwner() {
        List<Account> accountsMock = new ArrayList<>();
        accountsMock.add(getAccountTest());

        Mockito.when(accountService.findByCustomerOwnerId(Mockito.anyString()))
                .thenReturn(Flux.fromIterable(accountsMock));

        webClient.get().uri("/accounts/findByCustomerOwnerId/1")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Account.class)
                .hasSize(1);
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

    private AccountWithHoldersDto getAccountWithHoldersTest() {
        List<String> holders = new ArrayList<>();
        holders.add("1");
        holders.add("2");
        return new AccountWithHoldersDto(getAccountTest(), holders);
    }
}