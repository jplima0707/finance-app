package com.example.transaction_service.domain.models;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import com.example.transaction_service.domain.enums.TransactionStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(
  name = "transactions",
  uniqueConstraints = {
    @UniqueConstraint(columnNames = {"idempotency_key"})
  }
)
public class Transaction {

    @Id
    @Column(name = "transaction_id", nullable = false, unique = true)
    @GeneratedValue(strategy=GenerationType.UUID)
    private UUID transactionId;

    @Column(name = "source_account_id", nullable = false)
    private UUID sourceAccountId;

    @Column(name = "destination_account_id")
    private UUID destinationAccountId;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status;

    @Column(length = 500)
    private String description;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "idempotency_key", nullable = false, unique = true)
    private String idempotencyKey;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
        this.status = TransactionStatus.PENDING;
    }

    public Transaction(UUID sourceAccountId, UUID destinationAccountId, BigDecimal amount, String description) {
        this.sourceAccountId = sourceAccountId;
        this.destinationAccountId = destinationAccountId;
        this.amount = amount;
        this.description = description;
    }
}