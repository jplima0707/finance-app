package com.example.transaction_service.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.transaction_service.exceptions.ResourceNotFoundException;
import com.example.transaction_service.domain.dtos.requests.CreateUserDTO;
import com.example.transaction_service.domain.dtos.responses.UserDTO;
import lombok.RequiredArgsConstructor;

import com.example.transaction_service.mappers.UserMapper;
import com.example.transaction_service.domain.models.User;
import com.example.transaction_service.repositories.IUserRepository;

@Service
@RequiredArgsConstructor
public class UserService  {

    private final UserMapper userMapper;
    private final IUserRepository userRepository;

    public UserDTO getUserById(UUID userId) {
        return userRepository.findById(userId).map(userMapper::entityToUserDTO).orElseThrow(() -> {
            return new ResourceNotFoundException("User");
        });
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream().map(userMapper::entityToUserDTO).toList();
    }

    public UserDTO createUser(CreateUserDTO userDTO) {
        User createdUser = userMapper.createUserToEntity(userDTO);
        return userMapper.entityToUserDTO(userRepository.save(createdUser));
    }

    public UserDTO updateUser(UUID userId, CreateUserDTO userDTO) {
        User existingUser = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User"));
        existingUser.setName(userDTO.name());
        existingUser.setPhone(userDTO.phone());
        existingUser.setCpf(userDTO.cpf());
        existingUser.setEmail(userDTO.email());
        existingUser.setBirthDate(userDTO.birthDate());
        User updatedUser = userRepository.save(existingUser);
        return userMapper.entityToUserDTO(updatedUser);
    }

    public UserDTO deleteUser(UUID userId) {
        UserDTO userDto = getUserById(userId);
        userRepository.deleteById(userId);
        return userDto;
    }
    
}
