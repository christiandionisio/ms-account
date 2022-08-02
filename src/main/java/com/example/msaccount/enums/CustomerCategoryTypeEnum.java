package com.example.msaccount.enums;

public enum CustomerCategoryTypeEnum {
    VIP("VIP"),
    PYME("PYME"),
    GENERAL("GENERAL");
    private final String value;

    CustomerCategoryTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
