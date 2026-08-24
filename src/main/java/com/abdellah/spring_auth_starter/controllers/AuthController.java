package com.abdellah.spring_auth_starter.controllers;


import com.abdellah.spring_auth_starter.entity.User;
import com.abdellah.spring_auth_starter.enums.USER_ROLE;
import com.abdellah.spring_auth_starter.repository.UserRepository;
import com.abdellah.spring_auth_starter.security.requests.RegistrationRequest;
import com.abdellah.spring_auth_starter.security.responses.MessageResponse;
import com.abdellah.spring_auth_starter.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;


    @PostMapping("/register")
    public ResponseEntity<?> Register(@Valid @RequestBody RegistrationRequest request){

        authService.register(request);
        
        return ResponseEntity.ok(new MessageResponse("User registered successfully"));
    }
}
