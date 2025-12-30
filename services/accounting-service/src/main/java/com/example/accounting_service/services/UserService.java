package com.example.accounting_service.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.accounting_service.exceptions.ResourceNotFoundException;
import com.example.accounting_service.domain.dtos.requests.CreateUserDTO;
import com.example.accounting_service.domain.dtos.responses.UserDTO;
import com.example.accounting_service.services.interfaces.IUserService;

import lombok.RequiredArgsConstructor;

import com.example.accounting_service.mappers.UserMapper;
import com.example.accounting_service.domain.models.User;
import com.example.accounting_service.repositories.IUserRepository;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserMapper userMapper;
    private final IUserRepository userRepository;

    @Override
    public UserDTO getUserById(UUID userId) {
        return userRepository.findById(userId).map(userMapper::entityToUserDTO).orElseThrow(() -> {
            return new ResourceNotFoundException("User");
        });
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream().map(userMapper::entityToUserDTO).toList();
    }

    @Override
    public UserDTO createUser(CreateUserDTO userDTO) {
        User createdUser = userMapper.createUserToEntity(userDTO);
        return userMapper.entityToUserDTO(userRepository.save(createdUser));
    }

    @Override
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

    @Override
    public UserDTO deleteUser(UUID userId) {
        UserDTO userDto = getUserById(userId);
        userRepository.deleteById(userId);
        return userDto;
    }
    
}
