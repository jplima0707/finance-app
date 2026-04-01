package com.example.accounting_service.services;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.accounting_service.domain.dtos.types.TransactionValidationResult;
import com.example.accounting_service.domain.enums.AccountStatus;
import com.example.accounting_service.domain.enums.TransactionValidationErrors;
import com.example.accounting_service.domain.models.Account;
import com.example.accounting_service.repositories.IAccountRepository;
import com.jplima0707.common.events.TransactionRequestedEvent;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionConsumerService {

    private final IAccountRepository accountRepository;

    public TransactionValidationResult isTransactionValid(TransactionRequestedEvent event) {
        if (event.amount().compareTo(BigDecimal.ZERO) <= 0) {
            return new TransactionValidationResult(false, TransactionValidationErrors.INVALID_AMOUNT);
        }

        if (event.sourceAccountId() == null && event.destinationAccountId() == null) {
            return new TransactionValidationResult(false, TransactionValidationErrors.ACCOUNT_NOT_FOUND);
        }

        TransactionValidationResult sourceValidationResult = isSourceAccountValidForTransaction(event.sourceAccountId(), event.amount());
        if (!sourceValidationResult.isValid()) {
            return sourceValidationResult;
        }
        TransactionValidationResult destinationValidationResult = isDestinationAccountValidForTransaction(event.destinationAccountId());
        if (!destinationValidationResult.isValid()) {
            return destinationValidationResult;
        }

        return new TransactionValidationResult(true, null);
    }

    private TransactionValidationResult isSourceAccountValidForTransaction(UUID accountId, BigDecimal amount) {
        Account sourceAccount = accountRepository.findByAccountIdWithWriteLock(accountId)
                .orElse(null);
        
        if (sourceAccount == null) return new TransactionValidationResult(false, TransactionValidationErrors.ACCOUNT_NOT_FOUND);

        return sourceAccount.canDebit(amount);
    }

    private TransactionValidationResult isDestinationAccountValidForTransaction(UUID accountId) {
        Account destinationAccount = accountRepository.findByAccountIdWithWriteLock(accountId)
                .orElse(null);

        if (destinationAccount == null) return new TransactionValidationResult(false, TransactionValidationErrors.ACCOUNT_NOT_FOUND);

        if (destinationAccount.getAccountStatus() != AccountStatus.ACTIVE) return new TransactionValidationResult(false, TransactionValidationErrors.ACCOUNT_INACTIVE);

        return destinationAccount.canCredit();
    }

    public void debitSourceAccount(UUID accountId, BigDecimal amount) {
        Account sourceAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Source account not found"));
        sourceAccount.setBalance(sourceAccount.getBalance().subtract(amount));
        accountRepository.save(sourceAccount);
    }

    public void creditDestinationAccount(UUID accountId, BigDecimal amount) {
        Account destinationAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Destination account not found"));
        destinationAccount.setBalance(destinationAccount.getBalance().add(amount));
        accountRepository.save(destinationAccount);
    }

}
