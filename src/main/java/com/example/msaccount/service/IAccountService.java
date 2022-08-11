package com.example.msaccount.service;

import com.example.msaccount.dto.AccountCustomerDto;
import com.example.msaccount.dto.AccountWithHoldersDto;
import com.example.msaccount.dto.BalanceDto;
import com.example.msaccount.models.Account;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IAccountService {
  Flux<Account> findAll();

  Mono<Account> findById(String id);

  Mono<Account> create(AccountCustomerDto account);

  Mono<Account> update(Account account);

  Mono<Void> delete(String id);

  Flux<Account> findByHoldersId(String holdersId);

  Mono<AccountWithHoldersDto> addHolders(String holder, String accountId);

  Mono<BalanceDto> getBalance(String accountId);

  Flux<Account> findByCustomerOwnerId(String customerOwnerId);

  Mono<Account> findByCustomerOwnerIdAndAccountId(String customerOwnerId, String accountId);
}
