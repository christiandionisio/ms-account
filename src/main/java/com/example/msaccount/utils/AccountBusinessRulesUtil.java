package com.example.msaccount.utils;

import com.example.msaccount.dto.AccountWithHoldersDTO;
import com.example.msaccount.enums.CustomerTypeEnum;
import com.example.msaccount.error.AccountNotSupportedForMultipleHoldersException;
import com.example.msaccount.error.CustomerNotFoundException;
import com.example.msaccount.models.Account;
import com.example.msaccount.models.Customer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;

public class AccountBusinessRulesUtil {

    public static Mono<Customer> findCustomerById(String id) {
        return WebClient.create().get()
                .uri("http://localhost:9082/customers/" + id)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response ->
                        Mono.error(new CustomerNotFoundException(id))
                )
                .bodyToMono(Customer.class);
    }

    public static Mono<AccountWithHoldersDTO> validateSupportOfAccount(Account accountDB) {
        if (accountDB.getCustomerOwnerType().equalsIgnoreCase(CustomerTypeEnum.BUSINESS.getValue())) {
            return Mono.just(new AccountWithHoldersDTO(accountDB,
                    new ArrayList<>(Arrays.asList(accountDB.getCustomerOwnerId()))));
        } else {
            return Mono.error(new AccountNotSupportedForMultipleHoldersException());
        }
    }

    public static Mono<Long> getQuantityOfCreditCardsByCustomer(String customerId) {
        return WebClient.create().get()
                .uri("http://localhost:9084/credit-cards/count/" + customerId)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response ->
                        Mono.error(new Exception(customerId))
                )
                .bodyToMono(Long.class);
    }
}
