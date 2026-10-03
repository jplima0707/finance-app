package com.example.transaction_service.domain.dtos.responses;

import java.time.Instant;
import java.util.UUID;

public record AccountDTO(UUID id, UUID userId, Instant createdAt, Instant updatedAt, String status) {
}
