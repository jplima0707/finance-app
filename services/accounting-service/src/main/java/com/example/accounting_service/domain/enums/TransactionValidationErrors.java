package com.example.accounting_service.domain.enums;

public enum TransactionValidationErrors {
    BALANCE_INSUFFICIENT("Insufficient balance in source account"),
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
