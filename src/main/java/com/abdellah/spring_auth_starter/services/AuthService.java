package com.abdellah.spring_auth_starter.services;


import com.abdellah.spring_auth_starter.entity.User;
import com.abdellah.spring_auth_starter.enums.USER_ROLE;
import com.abdellah.spring_auth_starter.exception.APIException;
import com.abdellah.spring_auth_starter.payload.UserDTO;
import com.abdellah.spring_auth_starter.repository.UserRepository;
import com.abdellah.spring_auth_starter.security.requests.LoginRequest;
import com.abdellah.spring_auth_starter.security.requests.RegistrationRequest;
import com.abdellah.spring_auth_starter.security.services.UserDetailsImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;


    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;


    public AuthService(UserRepository userRepository,  PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }


    @Transactional
    public void register(RegistrationRequest registrationRequest){
        if(userRepository.existsByEmail(registrationRequest.getEmail())){
            throw new APIException("Email Already Exists");
        }

        User user = new User();
        user.setFirstName(registrationRequest.getFirstName());
        user.setLastName(registrationRequest.getLastName());
        user.setEmail(registrationRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        //default role as user
        user.setRole(USER_ROLE.USER);
        userRepository.save(user);
    }

    public Authentication login(LoginRequest loginRequest) throws  AuthenticationException {
        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
    }

    public UserDTO getCurrentUser(){
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName(userDetails.getFirstName());
        userDTO.setLastName(userDetails.getLastName());
        userDTO.setEmail(userDetails.getEmail());
        String role = userDetails.getAuthorities().stream().findFirst().orElseThrow().getAuthority();
        userDTO.setRole(USER_ROLE.valueOf(role));

        return userDTO;
    }
}
