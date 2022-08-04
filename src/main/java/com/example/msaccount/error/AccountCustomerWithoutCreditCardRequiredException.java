package com.example.msaccount.error;

public class AccountCustomerWithoutCreditCardRequiredException extends Exception {

  public AccountCustomerWithoutCreditCardRequiredException(String category) {
    super("Customer " + category + " should have a credit card product before to create an account");
  }

}
