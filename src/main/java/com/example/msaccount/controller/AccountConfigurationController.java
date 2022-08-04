package com.example.msaccount.controller;

import com.example.msaccount.dto.AccountConfigurationDto;
import com.example.msaccount.models.AccountConfiguration;
import com.example.msaccount.service.IAccountConfigurationService;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


/**
 * Controller layer of Account Product.
 *
 * @author Alisson Arteaga / Christian Dionisio
 * @version 1.0
 */
@RestController
@RequestMapping("/accounts/configurations")
public class AccountConfigurationController {

  @Autowired
  private IAccountConfigurationService service;

  private ModelMapper modelMapper = new ModelMapper();


  @GetMapping
  public Flux<AccountConfiguration> findAll() {
    return service.findAll();
  }

  @GetMapping("/{id}")
  public Mono<AccountConfiguration> findById(String id) {
    return service.findById(id);
  }

  @PostMapping
  public Mono<AccountConfiguration> create(@RequestBody AccountConfigurationDto accountConfigurationDto) {
    modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    return service.create(modelMapper.map(accountConfigurationDto, AccountConfiguration.class));
  }

  @PutMapping()
  public Mono<AccountConfiguration> update(@RequestBody AccountConfigurationDto accountConfigurationDto) {
    modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    return service.update(modelMapper.map(accountConfigurationDto, AccountConfiguration.class));
  }

  @DeleteMapping("/{id}")
  public Mono<Void> delete(String id) {
    return service.delete(id);
  }

  @GetMapping("/searchByAccountTypeAndName")
  public Mono<AccountConfiguration> findByAccountTypeAndName(@RequestParam("accountType") String accountType,
                                                             @RequestParam("name") String name) {
    return service.findByAccountTypeAndName(accountType, name);
  }


}
