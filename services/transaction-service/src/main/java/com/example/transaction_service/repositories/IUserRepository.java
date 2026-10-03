package com.example.transaction_service.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.transaction_service.domain.models.User;

public interface IUserRepository extends JpaRepository<User, UUID> {
}
