package com.example.msaccount.dto;

import com.example.msaccount.models.Account;
import lombok.Data;

import java.util.List;

@Data
public class AccountCustomerDTO {
    private Account account;
    private String holder;
}
