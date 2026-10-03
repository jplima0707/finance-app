package com.example.transaction_service.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.transaction_service.domain.dtos.requests.CreateAccountDTO;
import com.example.transaction_service.domain.dtos.responses.AccountDTO;
import com.example.transaction_service.domain.enums.AccountStatus;
import com.example.transaction_service.domain.models.Account;
import com.example.transaction_service.exceptions.ResourceNotFoundException;
import com.example.transaction_service.mappers.AccountMapper;
import com.example.transaction_service.repositories.IAccountRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService  {
    
    private final IAccountRepository accountRepository;
    private final AccountMapper accountMapper;

    public AccountDTO getAccountById(UUID accountId) {
        return accountMapper.entityToAccountDTO(
            accountRepository.findById(accountId).orElseThrow(() -> new ResourceNotFoundException("Account"))
        );
    }

    public AccountDTO getAccountByHolder(UUID userId) {
        return accountMapper.entityToAccountDTO(
            accountRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Account"))
        );
    }

    public List<AccountDTO> getAllAccounts() {
        return accountRepository.findAll().stream()
            .map(accountMapper::entityToAccountDTO)
            .toList();
    }

    public AccountDTO createAccount(CreateAccountDTO account) {
        return accountMapper.entityToAccountDTO(
            accountRepository.save(
                accountMapper.createAccountDTOToEntity(account)
            )
        );
    }

    public AccountDTO deleteAccount(UUID accountId) {
        AccountDTO accountDTO = getAccountById(accountId);
        accountRepository.deleteById(accountId);
        return accountDTO;
    }

    public AccountDTO updateAccount(UUID accountId, CreateAccountDTO account) {
        Account existingAccount = accountRepository.findById(accountId)
            .orElseThrow(() -> new ResourceNotFoundException("Account"));
        existingAccount.setUserId(account.userId());
        return accountMapper.entityToAccountDTO(accountRepository.save(existingAccount));
    }

    public AccountDTO updateAccountStatus(UUID accountId, String status) {
        try {
            Account existingAccount = accountRepository.findById(accountId).orElseThrow(() -> new ResourceNotFoundException("Account"));
            existingAccount.setAccountStatus(AccountStatus.valueOf(status.toUpperCase()));
            return accountMapper.entityToAccountDTO(
                accountRepository.save(existingAccount)
            );
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid account status: " + status, e);
        }
    }
}
