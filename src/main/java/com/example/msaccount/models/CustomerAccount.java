package com.example.msaccount.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "customer_account")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerAccount {
  @Id
  String id;
  String idCustomer;
  String idAccount;
}
