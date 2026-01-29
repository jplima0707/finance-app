package com.example.transaction_service.mappers;

import com.example.transaction_service.domain.dtos.requests.CreateTransactionDTO;
import com.example.transaction_service.domain.dtos.responses.TransactionDTO;
import com.example.transaction_service.domain.models.Transaction;

public class TransactionMapper {
    public TransactionDTO entityToDTO(Transaction transaction) {
        return new TransactionDTO(
            transaction.getTransactionId(),
            transaction.getSourceAccountId(),
            transaction.getDestinationAccountId(),
            transaction.getAmount(),
            transaction.getType(),
            transaction.getDescription(),
            transaction.getStatus(),
            transaction.getCreatedAt()
        );
    }

    public Transaction dtoToEntity(CreateTransactionDTO dto) {
        Transaction transaction = new Transaction();
        transaction.setSourceAccountId(dto.sourceAccountId());
        transaction.setDestinationAccountId(dto.destinationAccountId());
        transaction.setAmount(dto.amount());
        transaction.setType(dto.type());
        transaction.setDescription(dto.description());
        transaction.setIdempotencyKey(dto.idempotencyKey());
        return transaction;
    }
}
