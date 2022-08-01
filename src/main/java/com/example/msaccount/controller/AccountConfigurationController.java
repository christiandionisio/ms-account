package com.example.msaccount.controller;

import com.example.msaccount.models.AccountConfiguration;
import com.example.msaccount.service.IAccountConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/accounts/configurations")
public class AccountConfigurationController {

    @Autowired
    private IAccountConfigurationService service;

    @GetMapping
    public Flux<AccountConfiguration> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Mono<AccountConfiguration> findById(String id) {
        return service.findById(id);
    }

    @PostMapping
    public Mono<AccountConfiguration> create(@RequestBody AccountConfiguration account) {
        return service.create(account);
    }

    @PutMapping()
    public Mono<AccountConfiguration> update(@RequestBody AccountConfiguration account) {
        return service.update(account);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> delete(String id) {
        return service.delete(id);
    }

}
