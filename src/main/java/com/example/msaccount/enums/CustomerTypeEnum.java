package com.example.msaccount.enums;

public enum CustomerTypeEnum {
    BUSINESS("BUSINESS"),
    PERSONNEL("PERSONNEL");
    private final String value;

    CustomerTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
