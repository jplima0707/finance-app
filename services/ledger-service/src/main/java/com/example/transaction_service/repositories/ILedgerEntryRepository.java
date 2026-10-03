package com.example.transaction_service.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.transaction_service.domain.models.LedgerEntry;

public interface ILedgerEntryRepository extends JpaRepository<LedgerEntry, UUID> {

    Optional<List<LedgerEntry>> findByTransactionId(UUID transactionId);
    Optional<List<LedgerEntry>> findByAccountId(UUID accountId);
    Optional<List<LedgerEntry>> findByAccountIdAndTransactionId(UUID accountId, UUID transactionId);
    
}
