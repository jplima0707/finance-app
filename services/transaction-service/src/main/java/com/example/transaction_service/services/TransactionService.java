package com.example.transaction_service.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.transaction_service.domain.dtos.requests.CreateTransactionDTO;
import com.example.transaction_service.domain.dtos.responses.TransactionDTO;
import com.example.transaction_service.domain.dtos.responses.TransactionResult;
import com.example.transaction_service.domain.models.Account;
import com.example.transaction_service.domain.models.Transaction;
import com.example.transaction_service.kafka.TransactionPublisher;
import com.example.transaction_service.mappers.TransactionMapper;
import com.example.transaction_service.repositories.IAccountRepository;
import com.example.transaction_service.repositories.ITransactionRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j 
public class TransactionService {

    private final ITransactionRepository transactionRepository;
    private final IAccountRepository accountRepository;
    private final TransactionPublisher eventPublisher;
    private final TransactionMapper transactionMapper;
    
    @Transactional
    public TransactionResult createTransactionRequest(CreateTransactionDTO request) {

        log.info("Creating transaction request with idempotency key: {}", request.idempotencyKey());
        Optional<Transaction> existingTransaction = transactionRepository.findByIdempotencyKey(request.idempotencyKey());
        if (existingTransaction.isPresent()) {
            log.info("Transaction with idempotency key: {} already exists", request.idempotencyKey());
            return new TransactionResult(
            transactionMapper.entityToDTO(existingTransaction.get()),
            true
        );
        }

        Optional<Account> destinationAccount = accountRepository.findById(request.destinationAccountId());
        if (destinationAccount.isEmpty()) {
            log.error("Destination account not found for ID: {}", request.destinationAccountId());
            throw new IllegalArgumentException("Destination account not found");
        }

        Transaction transaction = transactionMapper.dtoToEntity(request);

        // out box pattern
        transactionRepository.save(transaction);

        String traceId = UUID.randomUUID().toString();

        eventPublisher.publishTransactionRequested(transaction, traceId);
        eventPublisher.publishTransactionCreated(transaction, traceId);

        log.info("Transaction request created successfully with ID: {} (traceId: {})", transaction.getTransactionId(), traceId);
        return new TransactionResult(
            transactionMapper.entityToDTO(transaction),
            false
        );
    }
    
    public List<TransactionDTO> getAllTransactionsBySourceAccountId(UUID accountId) {
        Optional<List<Transaction>> transactions = transactionRepository.findBySourceAccountId(accountId);
        return transactions.map(transactionList -> transactionList.stream()
                .map(transactionMapper::entityToDTO)
                .collect(Collectors.toList()))
            .orElse(null);
    }
    
    public List<TransactionDTO> getAllTransactionsByDestinationAccountId(UUID accountId) {
        Optional<List<Transaction>> transactions = transactionRepository.findByDestinationAccountId(accountId);
        return transactions.map(transactionList -> transactionList.stream()
                .map(transactionMapper::entityToDTO)
                .collect(Collectors.toList()))
            .orElse(null);
    }

    public List<TransactionDTO> getAllTransactionsByAccountId(UUID accountId) {
        Optional<List<Transaction>> transactions = transactionRepository.findBySourceAccountIdOrDestinationAccountId(accountId, accountId);
        return transactions.map(transactionList -> transactionList.stream()
                .map(transactionMapper::entityToDTO)
                .collect(Collectors.toList()))
            .orElse(null);
    }

}
