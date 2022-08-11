package com.example.msaccount.error;

public class CustomerHasCreditDebtException extends Exception {
    public CustomerHasCreditDebtException() {
        super("Customer has credit debt");
    }
}
