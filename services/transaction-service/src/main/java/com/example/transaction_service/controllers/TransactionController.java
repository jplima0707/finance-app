package com.example.transaction_service.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.transaction_service.domain.dtos.requests.CreateTransactionDTO;
import com.example.transaction_service.domain.dtos.responses.TransactionDTO;
import com.example.transaction_service.domain.dtos.responses.TransactionResult;
import com.example.transaction_service.services.TransactionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {
    
    private final TransactionService transactionService;

    @PostMapping("/")
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
    public ResponseEntity<List<TransactionDTO>> getTransactionsBySourceAccountId(@PathVariable UUID accountId) {
        List<TransactionDTO> transactions = transactionService.getAllTransactionsBySourceAccountId(accountId);
        if (transactions.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/destination/{accountId}")
    public ResponseEntity<List<TransactionDTO>> getTransactionsByDestinationAccountId(@PathVariable UUID accountId) {
        List<TransactionDTO> transactions = transactionService.getAllTransactionsByDestinationAccountId(accountId);
        if (transactions.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(transactions);
    }
}