package com.example.msaccount.controller;

import com.example.msaccount.models.Account;
import com.example.msaccount.service.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    private IAccountService service;

    @GetMapping()
    public Flux<Account> findAll() {
        return service.findAll();
    }

    @PostMapping()
    public Mono<Account> create(@RequestBody Account account) {
        return service.create(account);
    }

}
