package com.example.msaccount.dto;

import com.example.msaccount.models.Account;
import lombok.Data;

@Data
public class AccountCustomerDto {
  private Account account;
  private String holder;
}
