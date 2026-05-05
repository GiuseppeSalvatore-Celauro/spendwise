package com.celauro.SpendWise.controllers;

import com.celauro.SpendWise.dtos.JwtDTO;
import com.celauro.SpendWise.dtos.UserDTO;
import com.celauro.SpendWise.dtos.UserLoginDTO;
import com.celauro.SpendWise.dtos.UserRegisterDTO;
import com.celauro.SpendWise.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping("/register")
    public UserDTO createUser(@RequestBody @Valid UserRegisterDTO request){
        log.info("User try to register - email{}", request.getEmail());
        return userService.registerUser(request);
    }

    @PostMapping("/login")
    public JwtDTO loginUser(@RequestBody @Valid UserLoginDTO request){
        log.info("User try to login - email{}", request.getEmail());
        return userService.loginUser(request);
    }

}
