package com.example.transaction_service.controllers;

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


@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {
    
    private final TransactionService transactionService;

    @PostMapping("/")
    public ResponseEntity<TransactionDTO> createTransaction(@RequestBody @Valid CreateTransactionDTO request){
        TransactionResult result = transactionService.create(request);

        if (result.alreadyCreated()) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(result.transaction());
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(result.transaction());
    }
}
