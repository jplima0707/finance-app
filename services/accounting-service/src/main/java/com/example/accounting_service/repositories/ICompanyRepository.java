package com.example.accounting_service.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.accounting_service.domain.models.Company;

public interface ICompanyRepository extends JpaRepository<Company, UUID> {
}
