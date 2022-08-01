package com.example.msaccount.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@Document(collection = "account_configuration")
public class AccountConfiguration {

    @Id
    private String id;
    private String name;
    private Integer value;
    private String accountType;

}
