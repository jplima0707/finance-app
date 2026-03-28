package com.example.transaction_service.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.transaction_service.domain.models.Transaction;

public interface ITransactionRepository extends JpaRepository<Transaction, UUID> {

    Optional<Transaction> findByIdempotencyKey(String idempotencyKey);
    List<Transaction> findBySourceAccountId(UUID sourceAccountId);
    List<Transaction> findByDestinationAccountId(UUID destinationAccountId);    
}
