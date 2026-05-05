package com.celauro.SpendWise.services;

import com.celauro.SpendWise.dtos.JwtDTO;
import com.celauro.SpendWise.dtos.UserDTO;
import com.celauro.SpendWise.dtos.UserLoginDTO;
import com.celauro.SpendWise.dtos.UserRegisterDTO;
import com.celauro.SpendWise.entity.User;
import com.celauro.SpendWise.exceptions.NotFoundException;
import com.celauro.SpendWise.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserDTO registerUser(UserRegisterDTO request){
        User newUser = new User(
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getName(),
                request.getSurname()
        );

        userRepository.save(newUser);

        log.info("User register correctly - email={}", newUser.getEmail());
        return toDto(newUser);
    }

    public JwtDTO loginUser(UserLoginDTO request) {
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new NotFoundException("User not found"));

        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            log.warn("Login failed - email={}", request.getEmail());
            throw new RuntimeException("Invalid credentials");
        }

        log.info("User logged in - email={}", user.getEmail());

        JwtDTO token = new JwtDTO();
        token.setToken(jwtService.generatedToken(user.getEmail()));
        token.setExpDate(jwtService.getExpiration(token.getToken()));

        return token;
    }


//    Helper methods
    public User getCurrentUser(){
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return checkIfUserExist(email);
    }

    public UserDTO toDto(User user) {
        return new UserDTO(
          user.getId(),
          user.getEmail(),
          user.getName(),
          user.getSurname(),
          user.isActive(),
          user.getCreatedAt(),
          user.getUpdatedAt()
        );
    }

    private List<UserDTO> toListOfDto_User(List<User> users){
        return users.stream()
                .map(this::toDto)
                .toList();
    }

    private User checkIfUserExist(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
