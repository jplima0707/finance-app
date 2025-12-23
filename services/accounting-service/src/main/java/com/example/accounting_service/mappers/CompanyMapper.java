package com.example.accounting_service.mappers;

import org.springframework.stereotype.Component;

import com.example.accounting_service.domain.dtos.requests.CreateCompanyDTO;
import com.example.accounting_service.domain.dtos.responses.CompanyDTO;
import com.example.accounting_service.domain.models.Company;

@Component
public class CompanyMapper {
    
    public Company createCompanyToEntity(CreateCompanyDTO dto) {
        Company company = new Company();
        company.setName(dto.name());
        company.setCnpj(dto.cnpj());
        company.setAddress(dto.address());
        company.setEmail(dto.email());
        company.setPhone(dto.phone());
        return company;
    }

    public CompanyDTO entityToCompanyDTO(Company company) {
        return new CompanyDTO(
            company.getCompanyId(),
            company.getName(),
            company.getCnpj(),
            company.getAddress(),
            company.getPhone(),
            company.getEmail(),
            company.getCreatedAt()
        );
    }
}
