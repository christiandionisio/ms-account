package com.example.msaccount.utils;

import com.example.msaccount.dto.AccountWithHoldersDto;
import com.example.msaccount.dto.CreditCardDto;
import com.example.msaccount.dto.CreditDto;
import com.example.msaccount.enums.CustomerTypeEnum;
import com.example.msaccount.error.AccountNotSupportedForMultipleHoldersException;
import com.example.msaccount.error.CustomerNotFoundException;
import com.example.msaccount.models.Account;
import com.example.msaccount.models.Customer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class AccountBusinessRulesUtil {

  @Value("${customer.service.uri}")
  private String uriCustomerService;

  @Value("${card.service.uri}")
  private String uriCardService;

  @Value("${credit.service.uri}")
  private String uriCreditService;

  public Mono<Customer> findCustomerById(String id) {
    return WebClient.create().get()
        .uri(uriCustomerService + id)
        .accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .onStatus(HttpStatus::is4xxClientError, response ->
            Mono.error(new CustomerNotFoundException(id))
        )
        .bodyToMono(Customer.class);
  }

  public Mono<AccountWithHoldersDto> validateSupportOfAccount(Account accountDb) {
    if (accountDb.getCustomerOwnerType().equalsIgnoreCase(CustomerTypeEnum.BUSINESS.getValue())) {
      return Mono.just(new AccountWithHoldersDto(accountDb,
          new ArrayList<>(Arrays.asList(accountDb.getCustomerOwnerId()))));
    } else {
      return Mono.error(new AccountNotSupportedForMultipleHoldersException());
    }
  }

  public Mono<Long> getQuantityOfCreditCardsByCustomer(String customerId) {
    return WebClient.create().get()
        .uri(uriCardService+ "count/" + customerId)
        .accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .onStatus(HttpStatus::is4xxClientError, response ->
            Mono.error(new Exception(customerId))
        )
        .bodyToMono(Long.class);
  }

  public Flux<CreditDto> findCreditWithOverdueDebt(String idCustomer) {
    return WebClient.create().get()
            .uri( uriCreditService +
                    "creditWithOverdueDebt?" +
                    "customerId=" + idCustomer + "&date=" +
                    LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToFlux(CreditDto.class);
  }

  public Flux<CreditCardDto> getCreditCardsWithOverdueDebt(String idCustomer) {
    return WebClient.create().get()
            .uri(uriCardService +
                    "creditCardsWithOverdueDebt?" +
                    "customerId=" + idCustomer + "&hasDebt=true")
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToFlux(CreditCardDto.class);
  }


}
