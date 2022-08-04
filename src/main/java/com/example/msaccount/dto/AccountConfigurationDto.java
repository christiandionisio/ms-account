package com.example.msaccount.dto;

import lombok.Data;

@Data
public class AccountConfigurationDto {
  private String id;
  private String name;
  private Integer value;
  private String accountType;
}
