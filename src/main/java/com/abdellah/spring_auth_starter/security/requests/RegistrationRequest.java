package com.abdellah.spring_auth_starter.security.requests;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationRequest {

    @NotBlank
    @Size(min = 3, max =15, message = "User first name size must be between 3 and 15")
    private String firstName;

    @NotBlank
    @Size(min = 2, max =20, message = "User last name size must be between 2 and 20")
    private String lastName;

    @Size(max=50)
    @Email(message = "Invalid email format")
    private String email;

    @Size(min = 8, max = 30, message = "Password must be between 8 and 30")
    private String password;
}
