package com.example.accounting_service.domain.models;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "companies")
@Getter
@Setter
@NoArgsConstructor
public class Company {

    @Id
    @GeneratedValue(strategy=GenerationType.UUID)
    @Column(name = "id")
    private UUID companyId;

    @Column(nullable = false, length = 180)
    private String name;

    @Column(length = 18, unique = true)
    private String cnpj;

    private String address;

    @Column(length = 20, unique = true)
    private String phone;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(nullable = false, name = "created_at", updatable = false)
    private Instant createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
    }

    public Company(String name, String email, String cnpj, String address, String phone) {
        this.cnpj = cnpj;
        this.address = address;
        this.phone = phone;    
        this.name = name;
        this.email = email;
    }
    
}
