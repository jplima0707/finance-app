package com.example.transaction_service.repositories;

import java.util.Optional;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;

import com.example.transaction_service.domain.models.Account;

public interface  IAccountRepository extends JpaRepository<Account, UUID> {

    @EntityGraph(attributePaths = "user")
    Optional<Account> findByUser_UserId(UUID userId);

    @Override
    @EntityGraph(attributePaths = "user")
    List<Account> findAll();

    @Override
    @EntityGraph(attributePaths = "user")
    Optional<Account> findById(UUID accountId);
}
