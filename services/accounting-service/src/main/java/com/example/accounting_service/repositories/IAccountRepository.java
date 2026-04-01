package com.example.accounting_service.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import com.example.accounting_service.domain.enums.HolderType;
import com.example.accounting_service.domain.models.Account;

import jakarta.persistence.LockModeType;

public interface  IAccountRepository extends JpaRepository<Account, UUID> {

    Optional<Account> findByHolderIdAndHolderType(UUID holderId, HolderType holderType);
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Account> findByAccountIdWithWriteLock(UUID accountId);
}
