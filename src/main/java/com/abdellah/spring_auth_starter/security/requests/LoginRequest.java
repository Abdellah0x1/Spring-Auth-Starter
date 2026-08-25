package com.abdellah.spring_auth_starter.security.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginRequest {

    @Email
    @NotBlank
    private String email;

    @Size(min = 8, message = "Password must contain at least 8 characters")
    @NotBlank
    private String password;
}
