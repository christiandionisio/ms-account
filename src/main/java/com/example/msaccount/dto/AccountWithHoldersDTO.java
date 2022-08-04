package com.example.msaccount.dto;

import com.example.msaccount.models.Account;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AccountWithHoldersDto {

  private Account account;
  private List<String> holders;

}
