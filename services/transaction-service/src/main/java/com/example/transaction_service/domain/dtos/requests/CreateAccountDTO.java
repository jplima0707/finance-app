package com.example.transaction_service.domain.dtos.requests;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record CreateAccountDTO(

    @NotNull(message = "userId is required")
    UUID userId
) {}
