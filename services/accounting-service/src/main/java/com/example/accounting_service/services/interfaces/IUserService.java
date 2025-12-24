package com.example.accounting_service.services.interfaces;

import java.util.List;
import java.util.UUID;

import com.example.accounting_service.domain.dtos.requests.CreateUserDTO;
import com.example.accounting_service.domain.dtos.responses.UserDTO;

public interface IUserService {
    
    public UserDTO getUserById(UUID userId);
    public List<UserDTO> getAllUsers();
    public UserDTO createUser(CreateUserDTO userDTO);
    public UserDTO updateUser(UUID userId, CreateUserDTO userDTO);
    public UserDTO deleteUser(UUID userId);
}
