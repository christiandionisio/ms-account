package com.example.msaccount.error;

public class PersonalCustomerHasAccountException extends Exception {
    public PersonalCustomerHasAccountException() {
        super("Personal customer already have an account created");
    }
}
