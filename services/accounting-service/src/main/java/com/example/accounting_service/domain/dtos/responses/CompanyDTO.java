package com.example.accounting_service.domain.dtos.responses;

import java.time.Instant;
import java.util.UUID;

public record CompanyDTO(UUID id, String name, String cnpj, String address, String phone, String email, Instant createdAt) {
}
