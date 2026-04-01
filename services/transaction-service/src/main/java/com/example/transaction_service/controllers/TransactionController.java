package com.example.transaction_service.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.transaction_service.domain.dtos.requests.CreateTransactionDTO;
import com.example.transaction_service.domain.dtos.responses.TransactionDTO;
import com.example.transaction_service.domain.dtos.responses.TransactionResult;
import com.example.transaction_service.exceptions.handler.ApiErrorResponse;
import com.example.transaction_service.services.TransactionService;

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
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
@Tag(name = "Transactions", description = "Operations for creating and querying financial transactions")
public class TransactionController {
    
    private final TransactionService transactionService;

    @PostMapping("/")
    @Operation(
        summary = "Create a transaction request",
        description = "Creates a new transaction request. Returns 201 when a new transaction is created and 200 when the same idempotency key has already produced a transaction."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Transaction created successfully",
            content = @Content(schema = @Schema(implementation = TransactionDTO.class))
        ),
        @ApiResponse(
            responseCode = "200",
            description = "Transaction already existed for the provided idempotency key",
            content = @Content(schema = @Schema(implementation = TransactionDTO.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid transaction request",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Unexpected internal server error",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
        )
    })
    public ResponseEntity<TransactionDTO> createTransaction(@RequestBody @Valid CreateTransactionDTO request){
        TransactionResult result = transactionService.createTransactionRequest(request);

        if (result.alreadyCreated()) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(result.transaction());
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(result.transaction());
    }

    @GetMapping("/source/{accountId}")
    @Operation(
        summary = "List transactions by source account",
        description = "Returns all transactions where the provided account is the source account. If none are found, the response is 200 with an empty list."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Transactions found",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = TransactionDTO.class)))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid account identifier",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Unexpected internal server error",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
        )
    })
    public ResponseEntity<List<TransactionDTO>> getTransactionsBySourceAccountId(
        @Parameter(description = "Source account identifier", required = true)
        @PathVariable UUID accountId
    ) {
        List<TransactionDTO> transactions = transactionService.getAllTransactionsBySourceAccountId(accountId);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/destination/{accountId}")
    @Operation(
        summary = "List transactions by destination account",
        description = "Returns all transactions where the provided account is the destination account. If none are found, the response is 200 with an empty list."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Transactions found",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = TransactionDTO.class)))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid account identifier",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Unexpected internal server error",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
        )
    })
    public ResponseEntity<List<TransactionDTO>> getTransactionsByDestinationAccountId(
        @Parameter(description = "Destination account identifier", required = true)
        @PathVariable UUID accountId
    ) {
        List<TransactionDTO> transactions = transactionService.getAllTransactionsByDestinationAccountId(accountId);
        return ResponseEntity.ok(transactions);
    }
}
