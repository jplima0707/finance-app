package com.example.accounting_service.services.interfaces;

import java.util.List;
import java.util.UUID;

import com.example.accounting_service.domain.dtos.requests.CreateAccountDTO;
import com.example.accounting_service.domain.dtos.responses.AccountDTO;

import jakarta.validation.Valid;

public interface IAccountService {
    
    public AccountDTO getAccountById(UUID accountId);
    public AccountDTO getAccountByHolder(UUID holderId, String holderType);
    public List<AccountDTO> getAllAccounts();
    public AccountDTO createAccount(@Valid CreateAccountDTO account);
    public AccountDTO deleteAccount(UUID accountId);
    public AccountDTO updateAccount(UUID accountId, @Valid CreateAccountDTO account);
    public AccountDTO updateAccountStatus(UUID accountId, String status);
}
