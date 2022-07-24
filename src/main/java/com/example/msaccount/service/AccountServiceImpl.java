package com.example.msaccount.service;

import com.example.msaccount.dto.AccountCustomerDTO;
import com.example.msaccount.enums.CustomerType;
import com.example.msaccount.error.PersonalCustomerHasAccountException;
import com.example.msaccount.models.Account;
import com.example.msaccount.models.Customer;
import com.example.msaccount.repo.AccountRepository;
import com.example.msaccount.utils.AccountBusinessRulesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AccountServiceImpl implements IAccountService {

    @Autowired
    private AccountRepository repository;

    @Autowired
    private ICustomerAccountService customerAccountService;

    @Override
    public Flux<Account> findAll() {
        return repository.findAll();
    }

    @Override
    public Mono<Account> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public Mono<Account> create(AccountCustomerDTO accountCustomerDTO) {
        System.out.println("CREATE ACCOUNT");
        return AccountBusinessRulesUtil.findCustomerById(accountCustomerDTO.getHolder()).flatMap(customer -> {
            // Valida el tipo de cliente
            if (customer.getCustomerType().equalsIgnoreCase(CustomerType.BUSINESS.getValue())) {
                return repository.save(accountCustomerDTO.getAccount());
            } else {
                System.out.println("Cliente personal");
                Flux<Boolean> validateIfAccountExist = customerAccountService.findByIdCustomer(customer.getCustomerId())
                        .flatMap(customerAccount -> {
                            System.out.println("VALIDAR SI CLIENTE TIENE CUENTA");
                            return repository.findByAccountIdAndAccountType(customerAccount.getIdAccount(), accountCustomerDTO.getAccount().getAccountType())
                                    .flatMap(accountExist -> Mono.just(true) )
                                    .defaultIfEmpty(false);
                        });
                return validateIfAccountExist
                        .collectList()
                        .flatMap(values -> {
                            System.out.println("Valida si cuenta existe en lista");
                            if (values.contains(true)) {
                                return Mono.just(true);
                            } else {
                                return Mono.just(false);
                            }
                        })
                        .flatMap(customerHasAccount -> Boolean.TRUE.equals((customerHasAccount)) ?
                                Mono.error(new PersonalCustomerHasAccountException()):
                                repository.save(accountCustomerDTO.getAccount())
                        );

            }
        });
    }

    @Override
    public Mono<Account> update(Account account) {
        return repository.save(account);
    }

    @Override
    public void delete(String id) {
        repository.deleteById(id);
    }

    @Override
    public Flux<Account> findByHoldersId(String holdersId) {
        return null;
    }


}
