package com.example.msaccount.controller;

import com.example.msaccount.models.CustomerAccount;
import com.example.msaccount.service.ICustomerAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/customer-accounts")
public class CustomerAccountController {

    @Autowired
    private ICustomerAccountService service;

    @PostMapping
    public Mono<CustomerAccount> create (@RequestBody CustomerAccount customerAccount) {
        return service.save(customerAccount);
    }

    @GetMapping("/{idCustomer}")
    public Flux<CustomerAccount> findByCustomerId(@PathVariable String idCustomer) {
        return service.findByIdCustomer(idCustomer);
    }
}
