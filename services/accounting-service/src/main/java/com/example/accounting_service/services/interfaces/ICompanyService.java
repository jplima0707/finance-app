package com.example.accounting_service.services.interfaces;

import java.util.List;
import java.util.UUID;

import com.example.accounting_service.domain.dtos.requests.CreateCompanyDTO;
import com.example.accounting_service.domain.dtos.responses.CompanyDTO;

public interface ICompanyService {

    public List<CompanyDTO> getAllCompanies();
    public CompanyDTO getCompanyById(UUID id);
    public CompanyDTO createCompany(CreateCompanyDTO companyDTO);
    public CompanyDTO deleteCompany(UUID id);
    public CompanyDTO updateCompany(UUID id, CreateCompanyDTO companyDTO);
}