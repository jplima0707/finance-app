package com.example.accounting_service.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.accounting_service.domain.dtos.requests.CreateAccountDTO;
import com.example.accounting_service.domain.dtos.responses.AccountDTO;
import com.example.accounting_service.exceptions.handler.ApiErrorResponse;
import com.example.accounting_service.services.interfaces.IAccountService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
@Tag(name = "Accounts", description = "Operations for managing financial accounts")
public class AccountController {
    
    private final IAccountService accountService;

    @GetMapping("/")
    @Operation(
        summary = "List all accounts",
        description = "Returns every account currently available in the accounting service."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Accounts retrieved successfully",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = AccountDTO.class)))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Unexpected internal server error",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
        )
    })
    public ResponseEntity<List<AccountDTO>> getAllAccounts() {
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    @GetMapping("/accountByHolder")
    @Operation(
        summary = "Find an account by holder",
        description = "Returns the account associated with the given holder identifier and holder type."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Account retrieved successfully",
            content = @Content(schema = @Schema(implementation = AccountDTO.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid holder type or invalid request parameter",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Account not found",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Unexpected internal server error",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
        )
    })
    public ResponseEntity<AccountDTO> getAccountByHolder(
        @Parameter(description = "Holder identifier", required = true)
        @RequestParam("holderId") UUID holderId,
        @Parameter(description = "Holder type. Supported values: USER or COMPANY", required = true)
        @RequestParam("holderType") String holderType
    ) {
        return ResponseEntity.ok(accountService.getAccountByHolder(holderId, holderType));
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Get an account by id",
        description = "Returns the account that matches the provided account identifier."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Account retrieved successfully",
            content = @Content(schema = @Schema(implementation = AccountDTO.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid account identifier",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Account not found",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Unexpected internal server error",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
        )
    })
    public ResponseEntity<AccountDTO> getAccountById(@PathVariable UUID id) {
        return ResponseEntity.ok(accountService.getAccountById(id));
    }

    @PostMapping("/")
    @Operation(
        summary = "Create an account",
        description = "Creates a new account for an existing user or company holder."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Account created successfully",
            content = @Content(schema = @Schema(implementation = AccountDTO.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Validation error or invalid holder type",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Referenced holder not found",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Unexpected internal server error",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
        )
    })
    public ResponseEntity<AccountDTO> createAccount(@RequestBody @Valid CreateAccountDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.createAccount(dto));
    }    

    @PutMapping("/{id}")
    @Operation(
        summary = "Update an account",
        description = "Updates the holder information associated with an existing account."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Account updated successfully",
            content = @Content(schema = @Schema(implementation = AccountDTO.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Validation error, invalid account identifier, or invalid holder type",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Account or referenced holder not found",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Unexpected internal server error",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
        )
    })
    public ResponseEntity<AccountDTO> updateAccount(@PathVariable UUID id, @RequestBody @Valid CreateAccountDTO dto) {
        return ResponseEntity.ok(accountService.updateAccount(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Delete an account",
        description = "Deletes an account and returns the deleted account representation."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Account deleted successfully",
            content = @Content(schema = @Schema(implementation = AccountDTO.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid account identifier",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Account not found",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Unexpected internal server error",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
        )
    })
    public ResponseEntity<AccountDTO> deleteAccount(@PathVariable UUID id) {
        return ResponseEntity.ok(accountService.deleteAccount(id));
    }

    @PutMapping("/{id}/status")
    @Operation(
        summary = "Update account status",
        description = "Updates the lifecycle status of an account."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Account status updated successfully",
            content = @Content(schema = @Schema(implementation = AccountDTO.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid account identifier or invalid account status",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Account not found",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Unexpected internal server error",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
        )
    })
    public ResponseEntity<AccountDTO> updateAccountStatus(
        @PathVariable UUID id,
        @Parameter(description = "New account status", required = true)
        @RequestParam("status") String status
    ) {
        return ResponseEntity.ok(accountService.updateAccountStatus(id, status));
    }
    
}
