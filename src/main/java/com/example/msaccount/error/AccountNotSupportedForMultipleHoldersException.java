package com.example.msaccount.error;

public class AccountNotSupportedForMultipleHoldersException extends Exception {

  public AccountNotSupportedForMultipleHoldersException() {
    super("This type of account is not supoorted for multiples holders");
  }

}
