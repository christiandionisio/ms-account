package com.example.msaccount.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@Document(collection = "account_configuration")
public class AccountConfiguration {

    private String id;
    private String name;
    private String value;
    private String accountType;

}
