package com.example.accounting_service.domain.dtos.requests;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserDTO(
    
    @NotBlank(message = "name is required")
    @Size(max = 120, message = "name must be at most 120 characters")
    String name, 
    
    @Size(max = 20, message = "phone must be at most 20 characters")
    String phone, 
    
    @NotBlank(message = "cpf is required")
    @Size(max = 14, message = "cpf must be at most 14 characters")
    String cpf, 
    
    @NotBlank(message = "email is required")
    @Size(max = 150, message = "email must be at most 150 characters")
    String email, 
    
    LocalDate birthDate) {
    
}
