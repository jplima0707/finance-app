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
import com.example.accounting_service.services.interfaces.IAccountService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {
    
    private final IAccountService accountService;

    @GetMapping("/")
    public ResponseEntity<List<AccountDTO>> getAllAccounts() {
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    @GetMapping("/by-holder")
    public ResponseEntity<AccountDTO> getAccountByHolder(
        @RequestParam("holderId") UUID holderId,
        @RequestParam("holderType") String holderType
    ) {
        return ResponseEntity.ok(accountService.getAccountByHolder(holderId, holderType));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountDTO> getAccountById(@PathVariable UUID id) {
        return ResponseEntity.ok(accountService.getAccountById(id));
    }

    @PostMapping("/")
    public ResponseEntity<AccountDTO> createAccount(@RequestBody @Valid CreateAccountDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.createAccount(dto));
    }    

    @PutMapping("/{id}")
    public ResponseEntity<AccountDTO> updateAccount(@PathVariable UUID id, @RequestBody @Valid CreateAccountDTO dto) {
        return ResponseEntity.ok(accountService.updateAccount(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AccountDTO> deleteAccount(@PathVariable UUID id) {
        return ResponseEntity.ok(accountService.deleteAccount(id));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<AccountDTO> updateAccountStatus(@PathVariable UUID id, @RequestParam("status") String status) {
        return ResponseEntity.ok(accountService.updateAccountStatus(id, status));
    }
    
}
