package com.example.transaction_service.domain.models;

import java.time.Instant;
import java.util.UUID;

import com.example.transaction_service.domain.enums.AccountStatus;

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

    @Column(name = "user_id", nullable = false)
    @Setter
    private UUID userId;

    @Enumerated(EnumType.STRING)
    @Setter
    @Column(name = "status", nullable = false)
    private AccountStatus accountStatus; // "ACTIVE", "INACTIVE", "SUSPENDED", "BLOCKED"

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
    }
}
