package com.example.transaction_service.domain.dtos.responses;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import com.example.transaction_service.domain.enums.TransactionStatus;

public record TransactionDTO(
    UUID id,
    UUID sourceAccountId,
    UUID destinationAccountId,
    BigDecimal amount,
    String description,
    TransactionStatus status,
    Instant createdAt
) {
    
}
