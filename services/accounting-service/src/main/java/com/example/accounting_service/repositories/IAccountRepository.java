package com.example.accounting_service.repositories;

import java.util.UUID;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.accounting_service.domain.enums.HolderType;
import com.example.accounting_service.domain.models.Account;

public interface  IAccountRepository extends JpaRepository<Account, UUID> {

    Optional<Account> findByHolderIdAndHolderType(UUID holderId, HolderType holderType);
}
