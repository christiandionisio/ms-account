package com.example.msaccount.error;

public class CustomerNotFoundException extends Exception {

    public CustomerNotFoundException(String id) {
        super("Customer " + id + " not found");
    }

}
