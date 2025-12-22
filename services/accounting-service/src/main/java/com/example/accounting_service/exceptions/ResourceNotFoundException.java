package com.example.accounting_service.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String ResourceType) {
        super(ResourceType + " not found: ");
    }
}
