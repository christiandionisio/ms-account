package com.example.msaccount.error;

public class CustomerHasCreditCardDebtException extends Exception {
    public CustomerHasCreditCardDebtException() {
        super("Customer has credit card debt");
    }
}
