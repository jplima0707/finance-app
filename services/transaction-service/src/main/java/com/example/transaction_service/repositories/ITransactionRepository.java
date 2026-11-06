package com.example.transaction_service.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.transaction_service.domain.models.Transaction;

public interface ITransactionRepository extends JpaRepository<Transaction, UUID> {

    Optional<Transaction> findByIdempotencyKey(String idempotencyKey);
    Optional<List<Transaction>> findBySourceAccountId(UUID sourceAccountId);
    Optional<List<Transaction>> findByDestinationAccountId(UUID destinationAccountId);
    Optional<List<Transaction>> findBySourceAccountIdOrDestinationAccountId(UUID sourceAccountId, UUID destinationAccountId);    
}
