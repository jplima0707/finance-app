package com.example.transaction_service.services;

import org.springframework.stereotype.Service;

import com.example.transaction_service.domain.dtos.requests.CreateTransactionDTO;
import com.example.transaction_service.domain.dtos.responses.TransactionDTO;
import com.example.transaction_service.domain.models.Transaction;
import com.example.transaction_service.kafka.TransactionPublisher;
import com.example.transaction_service.mappers.TransactionMapper;
import com.example.transaction_service.repositories.ITransactionRepository;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final ITransactionRepository transactionRepository;
    private final TransactionPublisher eventPublisher;
    private final TransactionMapper transactionMapper;
    
    @Transactional
    public TransactionDTO create(@Valid CreateTransactionDTO request) {

        Transaction transaction = transactionMapper.dtoToEntity(request);

        transactionRepository.save(transaction);

        eventPublisher.publishTransactionRequested(transaction);

        return transactionMapper.entityToDTO(transaction);
    }
}
