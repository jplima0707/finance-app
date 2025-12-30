package com.example.transaction_service.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.transaction_service.domain.models.Transaction;

public interface ITransactionRepository extends JpaRepository<Transaction, UUID> {
    
}
