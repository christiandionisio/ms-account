package com.example.msaccount.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HolderAccountDto {
  private String holderId;
  private String accountId;
}
