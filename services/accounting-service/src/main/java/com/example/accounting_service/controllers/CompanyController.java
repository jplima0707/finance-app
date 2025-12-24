package com.example.accounting_service.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.accounting_service.domain.dtos.responses.CompanyDTO;
import com.example.accounting_service.services.interfaces.ICompanyService;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.accounting_service.domain.dtos.requests.CreateCompanyDTO;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/companies")
public class CompanyController {
    
    @Autowired
    private ICompanyService companyService;

    @GetMapping("/")
    public ResponseEntity<List<CompanyDTO>> getAllCompanies() {
        List<CompanyDTO> companies = companyService.getAllCompanies();
        return ResponseEntity.ok(companies);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyDTO> getCompanyById(@RequestParam UUID id) {
        CompanyDTO company = companyService.getCompanyById(id);
        return ResponseEntity.ok(company);
    }

    @PostMapping("/")
    public ResponseEntity<CompanyDTO> postMethodName(@RequestBody @Valid CreateCompanyDTO dto) {
        CompanyDTO company = companyService.createCompany(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(company);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<CompanyDTO> putMethodName(@PathVariable UUID id, @RequestBody @Valid CreateCompanyDTO companyDTO) {
        CompanyDTO company = companyService.updateCompany(id, companyDTO);
        return ResponseEntity.ok(company);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CompanyDTO> deleteCompany(@PathVariable UUID id) {
        CompanyDTO company = companyService.deleteCompany(id);
        return ResponseEntity.ok(company);
    }
}
