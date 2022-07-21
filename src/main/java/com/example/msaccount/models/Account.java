package com.example.msaccount.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "accounts")
@Data
@AllArgsConstructor
public class Account {

    private String accountId;
    private String accountNumber;
    private String accountType;
    private String state;
    private String balance;
    private String currency;
    private String createdAt;
    private String updatedAt;
    private String customerId;

}
