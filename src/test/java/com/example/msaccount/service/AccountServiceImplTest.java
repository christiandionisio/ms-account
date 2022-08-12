package com.example.msaccount.service;

import com.example.msaccount.dto.AccountWithHoldersDto;
import com.example.msaccount.dto.BalanceDto;
import com.example.msaccount.error.AccountInvalidBalanceException;
import com.example.msaccount.error.AccountToBusinessCustomerNotAllowedExecption;
import com.example.msaccount.error.PersonalCustomerHasAccountException;
import com.example.msaccount.models.Account;
import com.example.msaccount.models.CustomerAccount;
import com.example.msaccount.repo.AccountRepository;
import com.example.msaccount.utils.AccountBusinessRulesUtil;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.msaccount.provider.AccountServiceProvider.getAccountCustomerDtoCurrentAccountTest;
import static com.example.msaccount.provider.AccountServiceProvider.getAccountCustomerDtoCurrentAccountWithSavingAccountTypeTest;
import static com.example.msaccount.provider.AccountServiceProvider.getAccountCustomerDtoCurrentAccountWithZeroBalanceTest;
import static com.example.msaccount.provider.AccountServiceProvider.getAccountTest;
import static com.example.msaccount.provider.AccountServiceProvider.getAccountTestBusinessOwnerType;
import static com.example.msaccount.provider.AccountServiceProvider.getCustomerAccountTest;
import static com.example.msaccount.provider.AccountServiceProvider.getCustomerBusiness;
import static com.example.msaccount.provider.AccountServiceProvider.getCustomerBusinessPymeCategory;
import static com.example.msaccount.provider.AccountServiceProvider.getCustomerPersonal;
import static com.example.msaccount.provider.AccountServiceProvider.getCustomerPersonalVipCategory;

@SpringBootTest
class AccountServiceImplTest {

  @MockBean
  private AccountRepository repository;

  @MockBean
  private ICustomerAccountService customerAccountService;

  @MockBean
  private AccountBusinessRulesUtil accountBusinessRulesUtil;

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

  @Test
  void createAccountBusinessCustomerAndGeneralCategory() {

    Mockito.when(accountBusinessRulesUtil.findCustomerById(Mockito.anyString()))
            .thenReturn(Mono.just(getCustomerBusiness()));

    Mockito.when(accountBusinessRulesUtil.findCreditWithOverdueDebt(Mockito.anyString()))
            .thenReturn(Flux.empty());

    Mockito.when(accountBusinessRulesUtil.getCreditCardsWithOverdueDebt(Mockito.anyString()))
            .thenReturn(Flux.empty());

    Mockito.when(repository.save(Mockito.any(Account.class)))
            .thenReturn(Mono.just(getAccountTest()));

    Mono<Account> response = accountService.create(getAccountCustomerDtoCurrentAccountTest());
    StepVerifier.create(response)
            .expectNext(getAccountTest())
            .verifyComplete();

  }

  @Test
  void createAccountBusinessCustomerAndPymeCategory() {

    Mockito.when(accountBusinessRulesUtil.findCustomerById(Mockito.anyString()))
            .thenReturn(Mono.just(getCustomerBusinessPymeCategory()));

    Mockito.when(accountBusinessRulesUtil.findCreditWithOverdueDebt(Mockito.anyString()))
            .thenReturn(Flux.empty());

    Mockito.when(accountBusinessRulesUtil.getCreditCardsWithOverdueDebt(Mockito.anyString()))
            .thenReturn(Flux.empty());

    Mockito.when(accountBusinessRulesUtil.getQuantityOfCreditCardsByCustomer(Mockito.anyString()))
            .thenReturn(Mono.just(Long.valueOf(10)));

    Mockito.when(repository.save(Mockito.any(Account.class)))
            .thenReturn(Mono.just(getAccountTest()));

    Mono<Account> response = accountService.create(getAccountCustomerDtoCurrentAccountTest());
    StepVerifier.create(response)
            .expectNext(getAccountTest())
            .verifyComplete();

  }

  @Test
  void createAccountBusinessWithAccountInvalidBalanceException() {

    Mockito.when(accountBusinessRulesUtil.findCustomerById(Mockito.anyString()))
            .thenReturn(Mono.just(getCustomerBusiness()));

    Mockito.when(accountBusinessRulesUtil.findCreditWithOverdueDebt(Mockito.anyString()))
            .thenReturn(Flux.empty());

    Mockito.when(accountBusinessRulesUtil.getCreditCardsWithOverdueDebt(Mockito.anyString()))
            .thenReturn(Flux.empty());

    StepVerifier.create(accountService.create(getAccountCustomerDtoCurrentAccountWithZeroBalanceTest()))
            .expectError(AccountInvalidBalanceException.class)
            .verify();

  }

  @Test
  void createAccountBusinessSavingAccountTypeWithAccountToBusinessCustomerNotAllowedExecption() {

    Mockito.when(accountBusinessRulesUtil.findCustomerById(Mockito.anyString()))
            .thenReturn(Mono.just(getCustomerBusiness()));

    Mockito.when(accountBusinessRulesUtil.findCreditWithOverdueDebt(Mockito.anyString()))
            .thenReturn(Flux.empty());

    Mockito.when(accountBusinessRulesUtil.getCreditCardsWithOverdueDebt(Mockito.anyString()))
            .thenReturn(Flux.empty());

    StepVerifier.create(accountService.create(getAccountCustomerDtoCurrentAccountWithSavingAccountTypeTest()))
            .expectError(AccountToBusinessCustomerNotAllowedExecption.class)
            .verify();

  }


  @Test
  void createAccountPersonalCustomerAndGeneralCategoryWithPersonalCustomerHasAccountException() {

    Mockito.when(accountBusinessRulesUtil.findCustomerById(Mockito.anyString()))
            .thenReturn(Mono.just(getCustomerPersonal()));

    Mockito.when(accountBusinessRulesUtil.findCreditWithOverdueDebt(Mockito.anyString()))
            .thenReturn(Flux.empty());

    Mockito.when(accountBusinessRulesUtil.getCreditCardsWithOverdueDebt(Mockito.anyString()))
            .thenReturn(Flux.empty());

    List<CustomerAccount> customerAccounts = new ArrayList<>();
    customerAccounts.add(new CustomerAccount("1", "2", "3"));

    Mockito.when(customerAccountService.findByIdCustomer(Mockito.anyString()))
            .thenReturn(Flux.fromIterable(customerAccounts));

    Mockito.when(repository.findByAccountIdAndAccountType(Mockito.anyString(), Mockito.anyString()))
            .thenReturn(Mono.just(getAccountTest()));

    Mono<Account> response = accountService.create(getAccountCustomerDtoCurrentAccountTest());
    StepVerifier.create(response)
            .expectError(PersonalCustomerHasAccountException.class)
            .verify();
  }

  @Test
  void createAccountPersonalCustomerAndVipCategory() {

    Mockito.when(accountBusinessRulesUtil.findCustomerById(Mockito.anyString()))
            .thenReturn(Mono.just(getCustomerPersonalVipCategory()));

    Mockito.when(accountBusinessRulesUtil.findCreditWithOverdueDebt(Mockito.anyString()))
            .thenReturn(Flux.empty());

    Mockito.when(accountBusinessRulesUtil.getCreditCardsWithOverdueDebt(Mockito.anyString()))
            .thenReturn(Flux.empty());

    Mockito.when(customerAccountService.findByIdCustomer(Mockito.anyString()))
            .thenReturn(Flux.empty());

    Mockito.when(accountBusinessRulesUtil.getQuantityOfCreditCardsByCustomer(Mockito.anyString()))
            .thenReturn(Mono.just(Long.valueOf(10)));

    Mockito.when(repository.save(Mockito.any(Account.class)))
            .thenReturn(Mono.just(getAccountTest()));

    Mono<Account> response = accountService.create(getAccountCustomerDtoCurrentAccountTest());
    StepVerifier.create(response)
            .expectNext(getAccountTest())
            .verifyComplete();
  }

  @Test
  void updateAccount() {
    Mockito.when(repository.save(Mockito.any(Account.class)))
            .thenReturn(Mono.just(getAccountTest()));
    Mono<Account> response = accountService.update(getAccountTest());
    StepVerifier.create(response)
            .expectNext(getAccountTest())
            .verifyComplete();
  }

  @Test
  void deleteAccount() {
    Mockito.when(repository.deleteById(Mockito.anyString()))
            .thenReturn(Mono.empty());
    Mono<Void> response = accountService.delete("1");
    StepVerifier.create(response)
            .verifyComplete();
  }

  @Test
  void findByHoldersId() {
    Flux<Account> response = accountService.findByHoldersId("1");
    StepVerifier.create(response)
            .verifyComplete();
  }

  @Test
  void addHolder() {
    Mockito.when(repository.findById(Mockito.anyString()))
            .thenReturn(Mono.just(getAccountTestBusinessOwnerType()));

    Mockito.when(accountBusinessRulesUtil.validateSupportOfAccount(Mockito.any(Account.class)))
            .thenReturn(Mono.just(new AccountWithHoldersDto(getAccountTestBusinessOwnerType(),
                    new ArrayList<>(Arrays.asList(getCustomerAccountTest().getIdCustomer())))));

    Mockito.when(customerAccountService.save(Mockito.any(CustomerAccount.class)))
            .thenReturn(Mono.just(getCustomerAccountTest()));
    Mono<AccountWithHoldersDto> response = accountService.addHolders("1", "1");
    StepVerifier.create(response)
            .expectNext(new AccountWithHoldersDto(getAccountTestBusinessOwnerType(),
                    Arrays.asList(getCustomerAccountTest().getIdCustomer(), getCustomerAccountTest().getIdCustomer())))
            .verifyComplete();
  }

  @Test
  void getBalance() {
    Mockito.when(repository.findById(Mockito.anyString()))
            .thenReturn(Mono.just(getAccountTest()));
    Mono<BalanceDto> response = accountService.getBalance("1");
    StepVerifier.create(response)
            .expectNext(new BalanceDto(getAccountTest().getBalance(),
                    getAccountTest().getCurrency()))
            .verifyComplete();
  }

  @Test
  void findByCustomerOwnerId() {
    Mockito.when(repository.findByCustomerOwnerId(Mockito.anyString()))
            .thenReturn(Flux.just(getAccountTest()));
    Flux<Account> response = accountService.findByCustomerOwnerId("1");
    StepVerifier.create(response)
            .expectNext(getAccountTest())
            .verifyComplete();
  }

  @Test
  void findByCustomerOwnerIdAndAccountId() {
    Mockito.when(repository.findByCustomerOwnerIdAndAccountId(Mockito.anyString(), Mockito.anyString()))
            .thenReturn(Mono.just(getAccountTest()));
    Mono<Account> response = accountService.findByCustomerOwnerIdAndAccountId("1", "2");
    StepVerifier.create(response)
            .expectNext(getAccountTest())
            .verifyComplete();
  }

  @Test
  void findByCustomerOwnerIdAndCardId() {
    Mockito.when(repository.findByCustomerOwnerIdAndCardIdOrderByCardIdAssociateDateAsc(Mockito.anyString(), Mockito.anyString()))
            .thenReturn(Flux.just(getAccountTest()));
    Flux<Account> response = accountService.findByCustomerOwnerIdAndCardId("1", "2");
    StepVerifier.create(response)
            .expectNext(getAccountTest())
            .verifyComplete();
  }
}