package com.example.msaccount.utils;

import com.example.msaccount.error.CustomerNotFoundException;
import com.example.msaccount.models.Customer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class AccountBusinessRulesUtil {

    public static Mono<Customer> findCustomerById(String id) {
        return WebClient.create().get()
                .uri("http://localhost:8082/customers/" + id)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response ->
                        Mono.error(new CustomerNotFoundException(id))
                )
                .bodyToMono(Customer.class);
    }

}
