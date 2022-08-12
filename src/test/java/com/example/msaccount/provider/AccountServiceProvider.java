package com.example.msaccount.provider;

import com.example.msaccount.dto.AccountCustomerDto;
import com.example.msaccount.enums.CustomerTypeEnum;
import com.example.msaccount.models.Account;
import com.example.msaccount.models.Customer;
import com.example.msaccount.models.CustomerAccount;

import java.math.BigDecimal;

public class AccountServiceProvider {
  public static Customer getCustomerBusiness() {
    Customer customer = new Customer();
    customer.setCustomerId("1");
    customer.setCustomerType("BUSINESS");
    return customer;
  }

  public static Customer getCustomerPersonal() {
    Customer customer = new Customer();
    customer.setCustomerId("1");
    customer.setCustomerType("PERSONAL");
    return customer;
  }

  public static Customer getCustomerPersonalVipCategory() {
    Customer customer = new Customer();
    customer.setCustomerId("1");
    customer.setCustomerType("PERSONAL");
    customer.setCategory("VIP");
    return customer;
  }

  public static Customer getCustomerBusinessPymeCategory() {
    Customer customer = new Customer();
    customer.setCustomerId("1");
    customer.setCustomerType("BUSINESS");
    customer.setCategory("PYME");
    return customer;
  }

  public static AccountCustomerDto getAccountCustomerDtoCurrentAccountTest() {
    Account account = new Account();
    account.setAccountType("CORRIENTE");
    account.setBalance(BigDecimal.valueOf(1000));

    AccountCustomerDto accountCustomerDto = new AccountCustomerDto();
    accountCustomerDto.setAccount(account);
    accountCustomerDto.setHolder("1");
    return accountCustomerDto;
  }

  public static AccountCustomerDto getAccountCustomerDtoCurrentAccountWithZeroBalanceTest() {
    Account account = new Account();
    account.setAccountType("CORRIENTE");
    account.setBalance(BigDecimal.valueOf(-10));

    AccountCustomerDto accountCustomerDto = new AccountCustomerDto();
    accountCustomerDto.setAccount(account);
    accountCustomerDto.setHolder("1");
    return accountCustomerDto;
  }

  public static AccountCustomerDto getAccountCustomerDtoCurrentAccountWithSavingAccountTypeTest() {
    Account account = new Account();
    account.setAccountType("AHORRO");
    account.setBalance(BigDecimal.valueOf(1000));

    AccountCustomerDto accountCustomerDto = new AccountCustomerDto();
    accountCustomerDto.setAccount(account);
    accountCustomerDto.setHolder("1");
    return accountCustomerDto;
  }

  public static Account getAccountTest() {
    Account account = new Account();
    account.setAccountId("1");
    account.setAccountType("CORRIENTE");
    account.setBalance(BigDecimal.valueOf(1000));
    account.setCustomerOwnerId("1");
    account.setCustomerOwnerType("PERSONAL");
    return account;
  }

  public static Account getAccountTestBusinessOwnerType() {
    Account account = new Account();
    account.setAccountId("1");
    account.setAccountType("CORRIENTE");
    account.setBalance(BigDecimal.valueOf(1000));
    account.setCustomerOwnerId("abc");
    account.setCustomerOwnerType("BUSINESS");
    account.setCustomerOwnerType(CustomerTypeEnum.BUSINESS.getValue());
    return account;
  }

  public static CustomerAccount getCustomerAccountTest() {
    return CustomerAccount.builder()
            .id("1")
            .idCustomer("abc")
            .build();
  }
}
