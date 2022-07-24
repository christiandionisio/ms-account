package com.example.msaccount.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "customer_account")
@Data
@AllArgsConstructor
public class CustomerAccount {
    @Id
    String id;
    String idCustomer;
    String idAccount;
}
