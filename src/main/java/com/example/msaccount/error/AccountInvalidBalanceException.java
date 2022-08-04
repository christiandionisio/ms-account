package com.example.msaccount.error;

public class AccountInvalidBalanceException extends Exception {

  public AccountInvalidBalanceException() {
    super("Balance of the account should be bigger or equals to zero");
  }

}
