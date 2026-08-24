package com.abdellah.spring_auth_starter.services;


import com.abdellah.spring_auth_starter.entity.User;
import com.abdellah.spring_auth_starter.enums.USER_ROLE;
import com.abdellah.spring_auth_starter.exception.APIException;
import com.abdellah.spring_auth_starter.repository.UserRepository;
import com.abdellah.spring_auth_starter.security.requests.RegistrationRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;


    public AuthService(UserRepository userRepository,  PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

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
}
