package com.example.transaction_service.mappers;

import org.springframework.stereotype.Component;

import com.example.transaction_service.domain.dtos.requests.CreateAccountDTO;
import com.example.transaction_service.domain.dtos.responses.AccountDTO;
import com.example.transaction_service.domain.models.Account;
import com.example.transaction_service.domain.models.User;

@Component
public class AccountMapper {
    
    public Account createAccountDTOToEntity(CreateAccountDTO dto, User user) {
        Account account = new Account();
        account.setUser(user);
        return account;
    }

    public AccountDTO entityToAccountDTO(Account account) {
        return new AccountDTO(
            account.getAccountId(),
            account.getUser(),
            account.getCreatedAt(),
            account.getUpdatedAt(),
            account.getAccountStatus().name()
        );
    }
}
