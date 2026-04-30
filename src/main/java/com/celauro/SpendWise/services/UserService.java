package com.celauro.SpendWise.services;

import com.celauro.SpendWise.dtos.UserDTO;
import com.celauro.SpendWise.dtos.UserRegisterDTO;
import com.celauro.SpendWise.entity.User;
import com.celauro.SpendWise.exceptions.NotFoundException;
import com.celauro.SpendWise.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    public UserDTO saveUser(UserRegisterDTO request){
        User newUser = new User(
                request.getUsername(),
                request.getEmail(),
                request.getPassword()
        );

        userRepository.save(newUser);

        return toDto(newUser);
    }

    public List<UserDTO> getAllUser(){
        List<User> users = userRepository.findAll();
        return toListOfDto_User(users);
    }

    public User findOrThrowException(String email){
        return userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("user not found"));
    }

    public UserDTO toDto(User user) {
        return new UserDTO(
          user.getId(),
          user.getUsername(),
          user.getEmail(),
          user.isActive(),
          user.getCreatedAt(),
          user.getUpdatedAt()
        );
    }

    private List<UserDTO> toListOfDto_User(List<User> users){
        return users.stream()
                .map(user -> new UserDTO(
                        user.getId(),
                        user.getUsername(),
                        user.getEmail(),
                        user.isActive(),
                        user.getCreatedAt(),
                        user.getUpdatedAt()
                ))
                .toList();
    }
}
