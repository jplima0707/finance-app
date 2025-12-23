package com.example.accounting_service.domain.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCompanyDTO(

    @NotBlank(message = "name is required")
    @Size(max = 180, message = "name must be at most 180 characters")
    String name,

    @NotBlank(message = "cnpj is required")
    @Size(max = 18, message = "cnpj must be at most 18 characters")
    String cnpj,

    String address,

    @NotBlank(message = "phone is required")
    @Size(max = 20, message = "phone must be at most 20 characters")
    String phone,

    @NotBlank(message = "email is required")
    @Size(max = 150, message = "email must be at most 150 characters")
    String email
) {}
