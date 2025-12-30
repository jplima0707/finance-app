package com.example.transaction_service.domain.models;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import com.example.transaction_service.domain.enums.TransactionStatus;
import com.example.transaction_service.domain.enums.TransactionType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "transactions")
public class Transaction {

    @Id
    @Column(name = "transaction_id", nullable = false, unique = true)
    @GeneratedValue(strategy=GenerationType.UUID)
    private UUID transactionId;

    @Column(name = "account_id", nullable = false)
    private UUID accountId;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    @Column(length = 500)
    private String description;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    protected void onUpdate() {
        this.createdAt = Instant.now();
    }

    public Transaction(UUID accountId, BigDecimal amount, TransactionType type, TransactionStatus status, String description) {
        this.accountId = accountId;
        this.amount = amount;
        this.type = type;
        this.status = status;
        this.description = description;
    }
}