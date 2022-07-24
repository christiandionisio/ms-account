package com.example.msaccount.service;

import com.example.msaccount.dto.AccountCustomerDTO;
import com.example.msaccount.enums.AccountTypeEnum;
import com.example.msaccount.enums.CustomerTypeEnum;
import com.example.msaccount.error.AccountToBusinessCustomerNotAllowedExecption;
import com.example.msaccount.error.PersonalCustomerHasAccountException;
import com.example.msaccount.models.Account;
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
        return AccountBusinessRulesUtil.findCustomerById(accountCustomerDTO.getHolder()).flatMap(customer -> {
            // Valida el tipo de cliente
            if (customer.getCustomerType().equalsIgnoreCase(CustomerTypeEnum.BUSINESS.getValue())) {
                // CLIENTE EMPRESARIAL
                if (accountCustomerDTO.getAccount().getAccountType().equalsIgnoreCase(AccountTypeEnum.SAVING_ACCOUNT.getValue())
                    || accountCustomerDTO.getAccount().getAccountType().equalsIgnoreCase(AccountTypeEnum.DEPOSIT_ACCOUNT.getValue())) {
                    return Mono.error(new AccountToBusinessCustomerNotAllowedExecption());
                }
                return repository.save(accountCustomerDTO.getAccount());
            } else {
                // CLIENTE PERSONAL
                Flux<Boolean> validateIfAccountExist = customerAccountService.findByIdCustomer(customer.getCustomerId())
                        .flatMap(customerAccount -> {
                            // VALIDAR SI CLIENTE TIENE UNA CUENTA ASOCIADA
                            return repository.findByAccountIdAndAccountType(customerAccount.getIdAccount(),
                                            accountCustomerDTO.getAccount().getAccountType())
                                    .flatMap(accountExist -> Mono.just(true) )
                                    .defaultIfEmpty(false);
                        });
                return validateIfAccountExist
                        .collectList()
                        .flatMap(values -> (values.contains(true)) ? Mono.just(true): Mono.just(false))
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
