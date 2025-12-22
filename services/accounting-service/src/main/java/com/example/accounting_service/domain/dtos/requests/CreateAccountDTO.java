package com.example.accounting_service.domain.dtos.requests;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CreateAccountDTO(

    @NotNull(message = "holderId is required")
    UUID holderId,

    @NotBlank(message = "holderType is required")
    @Pattern(
        regexp = "USER|COMPANY",
        message = "holderType must be USER or COMPANY"
    )
    String holderType
) {}
