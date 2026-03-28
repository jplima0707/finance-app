package com.example.transaction_service.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.transaction_service.domain.dtos.requests.CreateTransactionDTO;
import com.example.transaction_service.domain.dtos.responses.TransactionDTO;
import com.example.transaction_service.domain.dtos.responses.TransactionResult;
import com.example.transaction_service.domain.models.Transaction;
import com.example.transaction_service.kafka.TransactionPublisher;
import com.example.transaction_service.mappers.TransactionMapper;
import com.example.transaction_service.repositories.ITransactionRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final ITransactionRepository transactionRepository;
    private final TransactionPublisher eventPublisher;
    private final TransactionMapper transactionMapper;
    
    @Transactional
    public TransactionResult createTransactionRequest(CreateTransactionDTO request) {

        Optional<Transaction> existingTransaction = transactionRepository.findByIdempotencyKey(request.idempotencyKey());
        if (existingTransaction.isPresent()) {
            return new TransactionResult(
            transactionMapper.entityToDTO(existingTransaction.get()),
            true
        );
        }

        Transaction transaction = transactionMapper.dtoToEntity(request);

        transactionRepository.save(transaction);

        eventPublisher.publishTransactionRequested(transaction);

        return new TransactionResult(
            transactionMapper.entityToDTO(transaction),
            false
        );
    }

    public List<TransactionDTO> getAllTransactionsBySourceAccountId(UUID accountId) {
        List<Transaction> transactions = transactionRepository.findBySourceAccountId(accountId);
        return transactions.stream()
                .map(transactionMapper::entityToDTO)
                .collect(Collectors.toList());
    }
    
    public List<TransactionDTO> getAllTransactionsByDestinationAccountId(UUID accountId) {
        List<Transaction> transactions = transactionRepository.findByDestinationAccountId(accountId);
        return transactions.stream()
                .map(transactionMapper::entityToDTO)
                .collect(Collectors.toList());
    }
}
