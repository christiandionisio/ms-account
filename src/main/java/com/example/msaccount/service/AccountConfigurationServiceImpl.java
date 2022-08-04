package com.example.msaccount.service;

import com.example.msaccount.models.AccountConfiguration;
import com.example.msaccount.repo.AccountConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AccountConfigurationServiceImpl implements IAccountConfigurationService {

  @Autowired
  private AccountConfigurationRepository repository;

  @Override
  public Flux<AccountConfiguration> findAll() {
    return repository.findAll();
  }

  @Override
  public Mono<AccountConfiguration> findById(String id) {
    return repository.findById(id);
  }

  @Override
  public Mono<AccountConfiguration> create(AccountConfiguration account) {
    return repository.save(account);
  }

  @Override
  public Mono<AccountConfiguration> update(AccountConfiguration account) {
    return repository.save(account);
  }

  @Override
  public Mono<Void> delete(String id) {
    return repository.deleteById(id);
  }

  @Override
  public Mono<AccountConfiguration> findByAccountTypeAndName(String accountType, String name) {
    return repository.findByAccountTypeAndName(accountType, name);
  }
}
