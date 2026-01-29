package com.example.transaction_service.domain.dtos.requests;

import java.math.BigDecimal;
import java.util.UUID;

import com.example.transaction_service.domain.enums.TransactionType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record CreateTransactionDTO(
    @NotNull
    UUID sourceAccountId,

    UUID destinationAccountId,

    @NotNull
    @Positive
    BigDecimal amount,

    @NotNull
    TransactionType type,

    @Size(max = 500)
    String description,

    @NotBlank
    String idempotencyKey
) {
    
}
