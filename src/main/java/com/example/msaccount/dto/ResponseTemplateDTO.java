package com.example.msaccount.dto;

import com.example.msaccount.models.Account;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseTemplateDTO {

    private Account account;
    private String errorMessage;

}
