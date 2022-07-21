package com.example.msaccount.service;

import com.example.msaccount.models.Account;
import com.example.msaccount.repo.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AccountServiceImpl implements IAccountService {

    @Autowired
    private AccountRepository repository;

    @Override
    public Flux<Account> findAll() {
        return repository.findAll();
    }

    @Override
    public Mono<Account> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public Mono<Account> create(Account account) {
        return repository.save(account);
    }

    @Override
    public Mono<Account> update(Account account) {
        return repository.save(account);
    }

    @Override
    public void delete(String id) {
        repository.deleteById(id);
    }
}
