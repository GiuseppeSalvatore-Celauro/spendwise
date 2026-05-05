package com.celauro.SpendWise.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterDTO {
    @NotBlank(message = "email field required")
    @Email
    private String email;

    @NotBlank(message = "password field required")
    private String password;

    @NotBlank(message = "name field required")
    private String name;

    @NotBlank(message = "surname field required")
    private String surname;
}
