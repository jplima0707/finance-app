package com.example.accounting_service.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.accounting_service.domain.dtos.requests.CreateCompanyDTO;
import com.example.accounting_service.domain.dtos.responses.CompanyDTO;
import com.example.accounting_service.domain.models.Company;
import com.example.accounting_service.exceptions.ResourceNotFoundException;
import com.example.accounting_service.mappers.CompanyMapper;
import com.example.accounting_service.repositories.ICompanyRepository;
import com.example.accounting_service.services.interfaces.ICompanyService;

import jakarta.validation.Valid;

@Service
public class CompanyService implements ICompanyService {

    @Autowired
    private ICompanyRepository companyRepository;

    @Autowired
    private CompanyMapper companyMapper;
    
    @Override
    public List<CompanyDTO> getAllCompanies() {
        return companyRepository.findAll().stream().map(companyMapper::entityToCompanyDTO).toList();
    }

    @Override
    public CompanyDTO getCompanyById(UUID id) {
        return companyMapper.entityToCompanyDTO(companyRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Company")));
    }

    @Override
    public CompanyDTO createCompany(@Valid CreateCompanyDTO companyDTO) {
        Company company = companyMapper.createCompanyToEntity(companyDTO);
        return companyMapper.entityToCompanyDTO(companyRepository.save(company));
    }

    @Override
    public CompanyDTO deleteCompany(UUID id) {
        CompanyDTO companyDto = getCompanyById(id);
        companyRepository.deleteById(id);
        return companyDto;
    }

    @Override
    public CompanyDTO updateCompany(UUID id, @Valid CreateCompanyDTO companyDTO) {
        Company existingCompany = companyRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Company"));
        existingCompany.setName(companyDTO.name());
        existingCompany.setCnpj(companyDTO.cnpj());
        existingCompany.setAddress(companyDTO.address());
        existingCompany.setEmail(companyDTO.email());
        existingCompany.setPhone(companyDTO.phone());
        Company updatedCompany = companyRepository.save(existingCompany);
        return companyMapper.entityToCompanyDTO(updatedCompany);
    }
    
}
