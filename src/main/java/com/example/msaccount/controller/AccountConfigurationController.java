package com.example.msaccount.controller;

import com.example.msaccount.dto.AccountConfigurationDTO;
import com.example.msaccount.models.AccountConfiguration;
import com.example.msaccount.service.IAccountConfigurationService;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    public Mono<AccountConfiguration> create(@RequestBody AccountConfigurationDTO accountConfigurationDTO) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return service.create(modelMapper.map(accountConfigurationDTO, AccountConfiguration.class));
    }

    @PutMapping()
    public Mono<AccountConfiguration> update(@RequestBody AccountConfigurationDTO accountConfigurationDTO) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return service.update(modelMapper.map(accountConfigurationDTO, AccountConfiguration.class));
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
