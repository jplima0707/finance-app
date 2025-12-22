package com.example.accounting_service.domain.dtos.requests;

public record CreateCompanyDTO(String name, String cnpj, String address, String phone, String email) {
    
}
