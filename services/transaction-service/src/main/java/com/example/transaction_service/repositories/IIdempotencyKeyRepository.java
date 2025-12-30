package com.example.transaction_service.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.transaction_service.domain.models.IdempotencyKey;

public interface IIdempotencyKeyRepository extends JpaRepository<IdempotencyKey, String> {
    
    Optional<IdempotencyKey> findByKey(String key);
    Optional<IdempotencyKey> findByTransactionId(UUID transactionId);
}
