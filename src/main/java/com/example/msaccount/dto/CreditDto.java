package com.example.msaccount.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreditDto {
    private String creditId;
    private BigDecimal creditBalance;
    private String paymentDate;
    private Integer timeLimit;
    private String initialDate;
    private BigDecimal monthlyFee;
    private String creditType;
    private String customerId;
    private Integer interestRate;
}
