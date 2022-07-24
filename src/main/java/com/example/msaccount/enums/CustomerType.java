package com.example.msaccount.enums;

public enum CustomerType {
    BUSINESS("BUSINESS"),
    PERSONNEL("PERSONNEL");
    private final String value;

    CustomerType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
