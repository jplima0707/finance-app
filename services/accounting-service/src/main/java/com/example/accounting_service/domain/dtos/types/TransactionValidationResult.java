package com.example.accounting_service.domain.dtos.types;

import com.example.accounting_service.domain.enums.TransactionValidationErrors;

public class TransactionValidationResult {
    private boolean valid;
    private TransactionValidationErrors reason;

    public TransactionValidationResult(boolean valid, TransactionValidationErrors reason) {
        this.valid = valid;
        this.reason = reason;
    }

    public boolean isValid() {
        return valid;
    }

    public String getReason() {
        return reason.getMessage();
    }
}
