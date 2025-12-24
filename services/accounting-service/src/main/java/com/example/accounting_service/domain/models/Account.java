package com.example.accounting_service.domain.models;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import com.example.accounting_service.domain.enums.AccountStatus;
import com.example.accounting_service.domain.enums.HolderType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "accounts")
@Getter
public class Account {
    
    @Id
    @GeneratedValue(strategy=GenerationType.UUID)
    @Column(name = "id")
    @Setter
    private UUID accountId;

    @Column(name = "holder_id", nullable = false)
    @Setter
    private UUID holderId; // Could refer to either User or Company

    @Enumerated(EnumType.STRING)
    @Setter
    @Column(name = "holder_type", nullable = false)
    private HolderType holderType; // "USER" or "COMPANY"

    @Enumerated(EnumType.STRING)
    @Setter
    @Column(name = "status", nullable = false)
    private AccountStatus accountStatus; // "ACTIVE", "INACTIVE", "SUSPENDED", "BLOCKED"

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balance;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    @Setter
    private Instant updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }

    public Account() {
        this.accountStatus = AccountStatus.ACTIVE;
        this.balance = BigDecimal.ZERO;
    }

    public Account(UUID holderId, HolderType holderType) {
        this.holderId = holderId;
        this.holderType = holderType;
        this.accountStatus = AccountStatus.ACTIVE;
        this.balance = BigDecimal.ZERO;
    }
}
