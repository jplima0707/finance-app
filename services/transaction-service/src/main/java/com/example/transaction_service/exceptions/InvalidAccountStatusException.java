package com.example.transaction_service.exceptions;

public class InvalidAccountStatusException extends RuntimeException {
    public InvalidAccountStatusException(String invalidStatus) {
        super("Invalid account status: " + invalidStatus);
    }
}
