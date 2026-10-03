package com.example.transaction_service.mappers;

import org.springframework.stereotype.Component;

import com.example.transaction_service.domain.dtos.requests.CreateAccountDTO;
import com.example.transaction_service.domain.dtos.responses.AccountDTO;
import com.example.transaction_service.domain.models.Account;

@Component
public class AccountMapper {
    
    public Account createAccountDTOToEntity(CreateAccountDTO dto) {
        Account account = new Account();
        account.setUserId(dto.userId());
        return account;
    }

    public AccountDTO entityToAccountDTO(Account account) {
        return new AccountDTO(
            account.getAccountId(),
            account.getUserId(),
            account.getCreatedAt(),
            account.getUpdatedAt(),
            account.getAccountStatus().name()
        );
    }
}
