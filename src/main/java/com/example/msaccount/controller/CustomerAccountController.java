package com.example.msaccount.controller;

import com.example.msaccount.dto.CustomerAccountDto;
import com.example.msaccount.models.CustomerAccount;
import com.example.msaccount.service.ICustomerAccountService;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/customer-accounts")
public class CustomerAccountController {

  @Autowired
  private ICustomerAccountService service;

  private ModelMapper modelMapper = new ModelMapper();

  @PostMapping
  public Mono<CustomerAccount> create(@RequestBody CustomerAccountDto customerAccountDto) {
    modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    return service.save(modelMapper.map(customerAccountDto, CustomerAccount.class));
  }

  @GetMapping("/{idCustomer}")
  public Flux<CustomerAccount> findByCustomerId(@PathVariable String idCustomer) {
    return service.findByIdCustomer(idCustomer);
  }
}
