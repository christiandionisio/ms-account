package com.example.msaccount.error;

public class HolderAlredyExistInAccountEException extends Exception {
  public HolderAlredyExistInAccountEException() {
    super("Customer already registered in the account holders");
  }
}
