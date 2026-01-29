package com.example.transaction_service.domain.dtos.responses;

public record TransactionResult(
    TransactionDTO transaction,
    boolean alreadyCreated
) {
    
}