package com.example.accounting_service.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.accounting_service.domain.models.User;

public interface IUserRepository extends JpaRepository<User, UUID> {
}
