package com.example.msaccount.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "account_configuration")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountConfiguration {

  @Id
  private String id;
  private String name;
  private Integer value;
  private String accountType;

}
