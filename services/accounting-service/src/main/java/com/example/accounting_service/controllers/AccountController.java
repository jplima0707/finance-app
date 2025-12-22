package com.example.accounting_service.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
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

@RestController
@RequestMapping("/accounts")
public class AccountController {
    
    @Autowired
    private IAccountService accountService;

    @GetMapping("/")
    public ResponseEntity<List<AccountDTO>> getAllAccounts() {
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    @GetMapping("/")
    public ResponseEntity<AccountDTO> getAccountByHolder(
        @RequestParam("holderId") String holderId,
        @RequestParam("holderType") String holderType
    ) {
        return ResponseEntity.ok(accountService.getAccountByHolder(UUID.fromString(holderId), holderType));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountDTO> getAccountById(@PathVariable String id) {
        return ResponseEntity.ok(accountService.getAccountById(UUID.fromString(id)));
    }

    @PostMapping("/")
    public ResponseEntity<AccountDTO> createAccount(@RequestBody CreateAccountDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.createAccount(dto));
    }    

    @PutMapping("/{id}")
    public ResponseEntity<AccountDTO> updateAccount(@PathVariable String id, @RequestBody CreateAccountDTO dto) {
        return ResponseEntity.ok(accountService.updateAccount(UUID.fromString(id), dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AccountDTO> deleteAccount(@PathVariable String id) {
        return ResponseEntity.ok(accountService.deleteAccount(UUID.fromString(id)));
    }
    
}
