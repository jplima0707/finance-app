package com.example.transaction_service.domain.enums;

public enum TransactionValidationErrors {
    ACCOUNT_INACTIVE("Account is not active"),
    ACCOUNT_NOT_FOUND("Account not found"),
    INVALID_AMOUNT("Amount must be greater than zero");

    private final String message;

    TransactionValidationErrors(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
