package com.example.msaccount.provider;

import com.example.msaccount.dto.AccountWithHoldersDto;
import com.example.msaccount.models.Account;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AccountControllerProvider {
  public static List<Account> getAccountList() {
    List<Account> accountsMock = new ArrayList<>();
    accountsMock.add(getAccountTest());
    return accountsMock;
  }

  public static Account getAccountTest() {
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

  public static AccountWithHoldersDto getAccountWithHoldersTest() {
    List<String> holders = new ArrayList<>();
    holders.add("1");
    holders.add("2");
    return new AccountWithHoldersDto(getAccountTest(), holders);
  }
}
