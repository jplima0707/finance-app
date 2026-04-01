package com.example.accounting_service.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.accounting_service.domain.dtos.responses.CompanyDTO;
import com.example.accounting_service.exceptions.handler.ApiErrorResponse;
import com.example.accounting_service.services.interfaces.ICompanyService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.accounting_service.domain.dtos.requests.CreateCompanyDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/v1/companies")
@RequiredArgsConstructor
@Tag(name = "Companies", description = "Operations for managing company holders")
public class CompanyController {
    
    private final ICompanyService companyService;

    @GetMapping("/")
    @Operation(
        summary = "List all companies",
        description = "Returns all company holders registered in the accounting service."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Companies retrieved successfully",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = CompanyDTO.class)))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Unexpected internal server error",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
        )
    })
    public ResponseEntity<List<CompanyDTO>> getAllCompanies() {
        List<CompanyDTO> companies = companyService.getAllCompanies();
        return ResponseEntity.ok(companies);
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Get a company by id",
        description = "Returns the company holder that matches the provided identifier."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Company retrieved successfully",
            content = @Content(schema = @Schema(implementation = CompanyDTO.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid company identifier",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Company not found",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Unexpected internal server error",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
        )
    })
    public ResponseEntity<CompanyDTO> getCompanyById(@PathVariable UUID id) {
        CompanyDTO company = companyService.getCompanyById(id);
        return ResponseEntity.ok(company);
    }

    @PostMapping("/")
    @Operation(
        summary = "Create a company",
        description = "Creates a new company holder."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Company created successfully",
            content = @Content(schema = @Schema(implementation = CompanyDTO.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Validation error",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Unexpected internal server error",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
        )
    })
    public ResponseEntity<CompanyDTO> createCompany(@RequestBody @Valid CreateCompanyDTO dto) {
        CompanyDTO company = companyService.createCompany(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(company);
    }
    
    @PutMapping("/{id}")
    @Operation(
        summary = "Update a company",
        description = "Updates the data of an existing company holder."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Company updated successfully",
            content = @Content(schema = @Schema(implementation = CompanyDTO.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Validation error or invalid company identifier",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Company not found",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Unexpected internal server error",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
        )
    })
    public ResponseEntity<CompanyDTO> updateCompany(@PathVariable UUID id, @RequestBody @Valid CreateCompanyDTO companyDTO) {
        CompanyDTO company = companyService.updateCompany(id, companyDTO);
        return ResponseEntity.ok(company);
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Delete a company",
        description = "Deletes a company holder and returns the deleted representation."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Company deleted successfully",
            content = @Content(schema = @Schema(implementation = CompanyDTO.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid company identifier",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Company not found",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Unexpected internal server error",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
        )
    })
    public ResponseEntity<CompanyDTO> deleteCompany(@PathVariable UUID id) {
        CompanyDTO company = companyService.deleteCompany(id);
        return ResponseEntity.ok(company);
    }
}
