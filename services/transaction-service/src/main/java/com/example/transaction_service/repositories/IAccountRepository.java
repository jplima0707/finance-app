package com.example.transaction_service.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.transaction_service.domain.models.Account;

public interface  IAccountRepository extends JpaRepository<Account, UUID> {

    Optional<Account> findByUserId(UUID userId);
}
