package com.example.msaccount.controller;

import com.example.msaccount.models.Account;
import com.example.msaccount.service.IAccountService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    private IAccountService service;

    private static final Logger logger = LogManager.getLogger(AccountController.class);

    @GetMapping()
    public Flux<Account> findAll() {
        logger.debug("Debugging log");
        logger.info("Info log");
        logger.warn("Hey, This is a warning!");
        logger.error("Oops! We have an Error. OK");
        logger.fatal("Damn! Fatal error. Please fix me.");
        return service.findAll();
    }

    @PostMapping()
    public Mono<Account> create(@RequestBody Account account) {
        return service.create(account);
    }

    @PutMapping
    public Mono<Account> update(@RequestBody Account account) {
        return service.update(account);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.delete(id);
    }

}
