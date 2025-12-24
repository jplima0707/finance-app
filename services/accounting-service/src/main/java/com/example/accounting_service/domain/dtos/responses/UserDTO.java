package com.example.accounting_service.domain.dtos.responses;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record UserDTO(UUID userId, String name, String phone, String cpf, String email, Instant createdAt, LocalDate birthDate) {
    
}
