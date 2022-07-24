package com.example.msaccount.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(collection = "accounts")
@Data
@AllArgsConstructor
public class Account {

    @Id
    private String accountId;
    private String accountNumber;
    private String accountType;
    private String state;
    private BigDecimal balance;
    private String currency;
    private String createdAt;
    private String updatedAt;
}
