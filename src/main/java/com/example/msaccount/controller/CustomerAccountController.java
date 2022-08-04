package com.example.msaccount.controller;

import com.example.msaccount.dto.CustomerAccountDTO;
import com.example.msaccount.models.CustomerAccount;
import com.example.msaccount.service.ICustomerAccountService;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/customer-accounts")
public class CustomerAccountController {

    @Autowired
    private ICustomerAccountService service;

    private ModelMapper modelMapper = new ModelMapper();

    @PostMapping
    public Mono<CustomerAccount> create (@RequestBody CustomerAccountDTO customerAccountDTO) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return service.save(modelMapper.map(customerAccountDTO, CustomerAccount.class));
    }

    @GetMapping("/{idCustomer}")
    public Flux<CustomerAccount> findByCustomerId(@PathVariable String idCustomer) {
        return service.findByIdCustomer(idCustomer);
    }
}
