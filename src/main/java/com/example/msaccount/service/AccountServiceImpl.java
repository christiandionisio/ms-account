package com.example.msaccount.service;

import com.example.msaccount.dto.AccountCustomerDTO;
import com.example.msaccount.dto.AccountWithHoldersDTO;
import com.example.msaccount.dto.BalanceDto;
import com.example.msaccount.enums.AccountTypeEnum;
import com.example.msaccount.enums.CustomerCategoryTypeEnum;
import com.example.msaccount.enums.CustomerTypeEnum;
import com.example.msaccount.error.*;
import com.example.msaccount.models.Account;
import com.example.msaccount.models.CustomerAccount;
import com.example.msaccount.repo.AccountRepository;
import com.example.msaccount.utils.AccountBusinessRulesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

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
        accountCustomerDTO.getAccount().setCustomerOwnerId(accountCustomerDTO.getHolder());
        return AccountBusinessRulesUtil.findCustomerById(accountCustomerDTO.getHolder()).flatMap(customer -> {

            if (accountCustomerDTO.getAccount().getBalance().compareTo(BigDecimal.ZERO) == -1) {
                return Mono.error(new AccountInvalidBalanceException());
            }

            accountCustomerDTO.getAccount().setCustomerOwnerType(customer.getCustomerType());
            // Valida el tipo de cliente
            if (customer.getCustomerType().equalsIgnoreCase(CustomerTypeEnum.BUSINESS.getValue())) {
                // CLIENTE EMPRESARIAL
                if (accountCustomerDTO.getAccount().getAccountType().equalsIgnoreCase(AccountTypeEnum.SAVING_ACCOUNT.getValue())
                    || accountCustomerDTO.getAccount().getAccountType().equalsIgnoreCase(AccountTypeEnum.DEPOSIT_ACCOUNT.getValue())) {
                    return Mono.error(new AccountToBusinessCustomerNotAllowedExecption());
                }

                String category = customer.getCategory();
                if(customer.getCategory() == null) category = CustomerCategoryTypeEnum.GENERAL.getValue();
                if(category.equals(CustomerCategoryTypeEnum.PYME.getValue())){
                    return AccountBusinessRulesUtil.getQuantityOfCreditCardsByCustomer(customer.getCustomerId())
                            .flatMap(count -> {
                                Integer countCreditCards = count.intValue();
                                if(countCreditCards.compareTo(0) == 1){
                                    return repository.save(accountCustomerDTO.getAccount());
                                }else{
                                    return Mono.error(new AccountCustomerWithoutCreditCardRequiredException(CustomerCategoryTypeEnum.PYME.getValue()));
                                }
                            });
                }else{
                    return repository.save(accountCustomerDTO.getAccount());
                }
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
                        .flatMap(customerHasAccount -> {
                            if(Boolean.TRUE.equals((customerHasAccount))){
                                return Mono.error(new PersonalCustomerHasAccountException());
                            }else{
                                String category = customer.getCategory();
                                if(customer.getCategory() == null) category = CustomerCategoryTypeEnum.GENERAL.getValue();
                                if(category.equals(CustomerCategoryTypeEnum.VIP.getValue())){
                                    return AccountBusinessRulesUtil.getQuantityOfCreditCardsByCustomer(customer.getCustomerId())
                                            .flatMap(count -> {
                                                Integer countCreditCards = count.intValue();
                                               if(countCreditCards.compareTo(0) == 1){
                                                   return repository.save(accountCustomerDTO.getAccount());
                                               }else{
                                                   return Mono.error(new AccountCustomerWithoutCreditCardRequiredException(CustomerCategoryTypeEnum.VIP.getValue()));
                                               }
                                            });
                                }else{
                                    return repository.save(accountCustomerDTO.getAccount());
                                }
                            }
                        });
            }
        });
    }

    @Override
    public Mono<Account> update(Account account) {
        return repository.save(account);
    }

    @Override
    public Mono<Void> delete(String id) {
        return repository.deleteById(id);
    }

    @Override
    public Flux<Account> findByHoldersId(String holdersId) {
        return null;
    }

    @Override
    public Mono<AccountWithHoldersDTO> addHolders(String holderId, String accountId) {
        return repository.findById(accountId)
                .flatMap(AccountBusinessRulesUtil::validateSupportOfAccount)
                .flatMap(accountWithHoldersDTO -> {
                    if (accountWithHoldersDTO.getHolders().contains(holderId)) {
                        return Mono.error(new HolderAlredyExistInAccountEException());
                    }

                    return customerAccountService.save(new CustomerAccount(null, holderId,
                                    accountWithHoldersDTO.getAccount().getAccountId()))
                            .flatMap(responseBD -> {
                               accountWithHoldersDTO.getHolders().add(responseBD.getIdCustomer());
                                return Mono.just(accountWithHoldersDTO);
                            });
                });
    }

    @Override
    public Mono<BalanceDto> getBalance(String accountId) {
        return repository.findById(accountId)
                .flatMap(account -> {
                    BalanceDto balanceDto = new BalanceDto(account.getBalance(), account.getCurrency());
                    Mono<BalanceDto> balanceDtoMono = Mono.just(balanceDto);
                    return balanceDtoMono;
                });
    }
}
