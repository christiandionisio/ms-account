package com.example.msaccount.models;

import lombok.Data;

import java.util.Date;

@Data
public class Customer {
  private String customerId;
  private String name;
  private String lastName;
  private String email;
  private String documentType;
  private String documentNumber;
  private Date birthDate;
  private String customerType;
  private String category;
}
