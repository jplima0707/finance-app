package com.example.accounting_service.mappers;

import org.springframework.stereotype.Component;

import com.example.accounting_service.domain.dtos.requests.CreateUserDTO;
import com.example.accounting_service.domain.dtos.responses.UserDTO;
import com.example.accounting_service.domain.models.User;

@Component
public class UserMapper {
    
    public UserDTO entityToUserDTO(User user) {
        return new UserDTO(
            user.getUserId(),
            user.getName(),
            user.getPhone(),
            user.getCpf(),
            user.getEmail(),
            user.getCreatedAt(),
            user.getBirthDate()
        );
    }

    public User createUserToEntity(CreateUserDTO dto) {
        User user = new User();
        user.setName(dto.name());
        user.setPhone(dto.phone());
        user.setCpf(dto.cpf());
        user.setEmail(dto.email());
        user.setBirthDate(dto.birthDate());
        return user;
    }
}
