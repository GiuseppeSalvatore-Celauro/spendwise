package com.celauro.SpendWise.controllers;

import com.celauro.SpendWise.dtos.UserDTO;
import com.celauro.SpendWise.dtos.UserLoginDTO;
import com.celauro.SpendWise.dtos.UserRegisterDTO;
import com.celauro.SpendWise.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/spendwise/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping("/register")
    public UserDTO createUser(@RequestBody @Valid UserRegisterDTO request){
        return userService.registerUser(request);
    }

    @PostMapping("/login")
    public String loginUser(@RequestBody @Valid UserLoginDTO request){
        return userService.loginUser(request);
    }

}
