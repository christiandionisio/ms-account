package com.example.msaccount.enums;

public enum AccountTypeEnum {
  SAVING_ACCOUNT("AHORRO"),
  CURRENT_ACCOUNT("CORRIENTE"),
  DEPOSIT_ACCOUNT("PLAZO_FIJO");
  private final String value;

  AccountTypeEnum(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
