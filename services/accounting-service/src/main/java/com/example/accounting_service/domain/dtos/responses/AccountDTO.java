package com.example.accounting_service.domain.dtos.responses;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record AccountDTO(UUID id, UUID holderId, String accountType, BigDecimal balance, Instant createdAt, Instant updatedAt, String status) {
}
