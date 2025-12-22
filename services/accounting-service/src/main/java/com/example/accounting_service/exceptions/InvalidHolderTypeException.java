package com.example.accounting_service.exceptions;

public class InvalidHolderTypeException extends RuntimeException {
    public InvalidHolderTypeException(String invalidType) {
        super("Invalid holder type: " + invalidType);
    }
}
