package com.example.msaccount.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class BalanceDto {
  private BigDecimal availableBalance;
  private String currency;
}
