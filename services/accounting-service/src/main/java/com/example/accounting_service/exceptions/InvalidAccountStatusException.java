package com.example.accounting_service.exceptions;

public class InvalidAccountStatusException extends RuntimeException {
    public InvalidAccountStatusException(String invalidStatus) {
        super("Invalid account status: " + invalidStatus);
    }
}
