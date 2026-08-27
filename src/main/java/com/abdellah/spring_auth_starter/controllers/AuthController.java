package com.abdellah.spring_auth_starter.controllers;


import com.abdellah.spring_auth_starter.enums.USER_ROLE;
import com.abdellah.spring_auth_starter.payload.UserDTO;
import com.abdellah.spring_auth_starter.security.requests.LoginRequest;
import com.abdellah.spring_auth_starter.security.requests.RegistrationRequest;
import com.abdellah.spring_auth_starter.security.responses.MessageResponse;
import com.abdellah.spring_auth_starter.security.services.JwtService;
import com.abdellah.spring_auth_starter.security.services.UserDetailsImpl;
import com.abdellah.spring_auth_starter.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtService jwtService;


    @PostMapping("/register")
    public ResponseEntity<?> Register(@Valid @RequestBody RegistrationRequest request){

        authService.register(request);

        return ResponseEntity.ok(new MessageResponse("User registered successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request){

        Authentication authentication = authService.login(request);

        UserDetailsImpl  userDetails = (UserDetailsImpl) authentication.getPrincipal();


        ResponseCookie authCookie = jwtService.generatejwtCookie(userDetails);

        String role = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList().get(0);

        UserDTO userDTO = new UserDTO(userDetails.getFirstName(), userDetails.getLastName(), userDetails.getEmail(), USER_ROLE.valueOf(role));

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, authCookie.toString()).body(userDTO);
    }
}
