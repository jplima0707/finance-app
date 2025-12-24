package com.example.accounting_service.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.accounting_service.domain.dtos.requests.CreateAccountDTO;
import com.example.accounting_service.domain.dtos.responses.AccountDTO;
import com.example.accounting_service.domain.enums.AccountStatus;
import com.example.accounting_service.domain.models.Account;
import com.example.accounting_service.domain.enums.HolderType;
import com.example.accounting_service.exceptions.ResourceNotFoundException;
import com.example.accounting_service.exceptions.InvalidHolderTypeException;
import com.example.accounting_service.repositories.IAccountRepository;
import com.example.accounting_service.services.interfaces.IAccountService;
import com.example.accounting_service.mappers.AccountingMapper;

@Service
public class AccountService implements IAccountService {
    
    @Autowired
    private IAccountRepository accountRepository;

    @Autowired
    private AccountingMapper accountingMapper;

    @Override
    public AccountDTO getAccountById(UUID accountId) {
        return accountingMapper.entityToAccountDTO(
            accountRepository.findById(accountId).orElseThrow(() -> new ResourceNotFoundException("Account"))
        );
    }

    @Override
    public AccountDTO getAccountByHolder(UUID holderId, String holderType) {
        try {
            return accountingMapper.entityToAccountDTO(
                accountRepository.findByHolderIdAndHolderType(holderId, HolderType.valueOf(holderType))
                    .orElseThrow(() -> new ResourceNotFoundException("Account"))
            );
        } catch (IllegalArgumentException e) {
            throw new InvalidHolderTypeException(holderType);
        }
        
    }

    @Override
    public List<AccountDTO> getAllAccounts() {
        return accountRepository.findAll().stream()
            .map(accountingMapper::entityToAccountDTO)
            .toList();
    }

    @Override
    public AccountDTO createAccount(CreateAccountDTO account) {
        return accountingMapper.entityToAccountDTO(
            accountRepository.save(
                accountingMapper.createAccountDTOToEntity(account)
            )
        );
    }

    @Override
    public AccountDTO deleteAccount(UUID accountId) {
        AccountDTO accountDTO = getAccountById(accountId);
        accountRepository.deleteById(accountId);
        return accountDTO;
    }

    @Override
    public AccountDTO updateAccount(UUID accountId, CreateAccountDTO account) {
        try { 
            Account existingAccount = accountRepository.findById(accountId).orElseThrow(() -> new ResourceNotFoundException("Account"));
            existingAccount.setHolderId(account.holderId());
            existingAccount.setHolderType(HolderType.valueOf(account.holderType()));
            return accountingMapper.entityToAccountDTO(
                accountRepository.save(existingAccount)
            );
        } catch (Exception e) {
            throw new InvalidHolderTypeException(account.holderType());
        }
    }

    @Override
    public AccountDTO updateAccountStatus(UUID accountId, String status) {
        try {
            Account existingAccount = accountRepository.findById(accountId).orElseThrow(() -> new ResourceNotFoundException("Account"));
            existingAccount.setAccountStatus(AccountStatus.valueOf(status.toUpperCase()));
            return accountingMapper.entityToAccountDTO(
                accountRepository.save(existingAccount)
            );
        } catch (IllegalArgumentException e) {
            throw new InvalidHolderTypeException(status);
        }
    }
}
