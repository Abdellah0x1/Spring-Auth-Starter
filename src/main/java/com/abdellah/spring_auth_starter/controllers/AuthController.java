package com.abdellah.spring_auth_starter.controllers;


import com.abdellah.spring_auth_starter.entity.User;
import com.abdellah.spring_auth_starter.enums.USER_ROLE;
import com.abdellah.spring_auth_starter.repository.UserRepository;
import com.abdellah.spring_auth_starter.security.requests.RegistrationRequest;
import com.abdellah.spring_auth_starter.security.responses.MessageResponse;
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
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> Register(@Valid @RequestBody RegistrationRequest request){

//        check if user with this email already exists
        if(userRepository.existsByEmail(request.getEmail())){
            return ResponseEntity.badRequest().body(new MessageResponse("Email already exists"));
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
//        set role to user by default
        user.setRole(USER_ROLE.USER);

        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully"));
    }
}
