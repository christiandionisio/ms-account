package com.example.msaccount.error;

public class AccountToBusinessCustomerNotAllowedExecption extends Exception {

  public AccountToBusinessCustomerNotAllowedExecption() {
    super("Business customer is not allowed to create this type of account");
  }
}
