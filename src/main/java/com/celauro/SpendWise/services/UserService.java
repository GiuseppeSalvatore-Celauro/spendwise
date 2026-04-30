package com.celauro.SpendWise.services;

import com.celauro.SpendWise.dtos.UserDTO;
import com.celauro.SpendWise.dtos.UserLoginDTO;
import com.celauro.SpendWise.dtos.UserRegisterDTO;
import com.celauro.SpendWise.entity.User;
import com.celauro.SpendWise.exceptions.NotFoundException;
import com.celauro.SpendWise.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayDeque;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService{
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

        return toDto(newUser);
    }

    public List<UserDTO> getAllUser(){
        List<User> users = userRepository.findAll();
        return toListOfDto_User(users);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = checkIfUserExist(email);

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                new ArrayDeque<>()
        );
    }

    public String loginUser(UserLoginDTO request) {
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new NotFoundException("User not found"));

        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            throw new RuntimeException("Invalid credentials");
        }

        return jwtService.generatedToken(user.getEmail());
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
