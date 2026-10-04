package com.example.transaction_service.domain.dtos.responses;

import java.time.Instant;
import java.util.UUID;

import com.example.transaction_service.domain.models.User;

public record AccountDTO(UUID id, User user, Instant createdAt, Instant updatedAt, String status) {
}
