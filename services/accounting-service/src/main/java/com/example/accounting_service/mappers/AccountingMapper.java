package com.example.accounting_service.mappers;

import org.springframework.stereotype.Component;

import com.example.accounting_service.domain.dtos.requests.CreateAccountDTO;
import com.example.accounting_service.domain.dtos.responses.AccountDTO;
import com.example.accounting_service.domain.enums.HolderType;
import com.example.accounting_service.domain.models.Account;
import com.example.accounting_service.exceptions.InvalidHolderTypeException;

@Component
public class AccountingMapper {
    
    public Account createAccountDTOToEntity(CreateAccountDTO dto) {
        Account account = new Account();
        account.setHolderId(dto.holderId());
        try {
            account.setHolderType(HolderType.valueOf(dto.holderType()));
        } catch (IllegalArgumentException e) {
            throw new InvalidHolderTypeException(dto.holderType());
        }
        return account;
    }

    public AccountDTO entityToAccountDTO(Account account) {
        return new AccountDTO(
            account.getAccountId(),
            account.getHolderId(),
            account.getHolderType().name(),
            account.getBalance(),
            account.getCreatedAt(),
            account.getUpdatedAt(),
            account.getAccountStatus().name()
        );
    }
}
