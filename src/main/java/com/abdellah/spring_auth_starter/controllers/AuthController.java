package com.abdellah.spring_auth_starter.controllers;


import com.abdellah.spring_auth_starter.entity.RefreshToken;
import com.abdellah.spring_auth_starter.entity.User;
import com.abdellah.spring_auth_starter.enums.USER_ROLE;
import com.abdellah.spring_auth_starter.payload.UserDTO;
import com.abdellah.spring_auth_starter.repository.RefreshTokenRepository;
import com.abdellah.spring_auth_starter.repository.UserRepository;
import com.abdellah.spring_auth_starter.security.requests.LoginRequest;
import com.abdellah.spring_auth_starter.security.requests.RegistrationRequest;
import com.abdellah.spring_auth_starter.security.responses.MessageResponse;
import com.abdellah.spring_auth_starter.security.services.JwtService;
import com.abdellah.spring_auth_starter.security.services.RefreshTokenService;
import com.abdellah.spring_auth_starter.security.services.UserDetailsImpl;
import com.abdellah.spring_auth_starter.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    

    @Autowired
    private UserRepository userRepository;


    @PostMapping("/register")
    public ResponseEntity<?> Register(@Valid @RequestBody RegistrationRequest request){

        authService.register(request);

        return ResponseEntity.ok(new MessageResponse("User registered successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request){

        Authentication authentication = authService.login(request);

        UserDetailsImpl  userDetails = (UserDetailsImpl) authentication.getPrincipal();

        User user = userRepository.findByEmail(userDetails.getEmail()).orElseThrow();

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        ResponseCookie refreshCookie = refreshTokenService.getRefreshTokenCookie(refreshToken);

        ResponseCookie authCookie = jwtService.generatejwtCookie(userDetails);

        String role = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList().get(0);

        UserDTO userDTO = new UserDTO(userDetails.getFirstName(), userDetails.getLastName(), userDetails.getEmail(), USER_ROLE.valueOf(role));

        return ResponseEntity.ok().
                header(HttpHeaders.SET_COOKIE, refreshCookie.toString()).
                header(HttpHeaders.SET_COOKIE, authCookie.toString()).body(userDTO);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@CookieValue(name = "refreshToken", required = false) String refreshToken){
        ResponseCookie cleanJwtCookie = jwtService.getCleanJwtCookie();
        ResponseCookie cleanRefreshTokenCookie = refreshTokenService.getCleanRefreshTokenCookie();

        if(refreshToken != null){
            refreshTokenService.deleteByToken(refreshToken);
        }



        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cleanJwtCookie.toString())
                .header(HttpHeaders.SET_COOKIE, cleanRefreshTokenCookie.toString())
                .body(new MessageResponse("You have been logged out"));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@CookieValue(name="refreshToken") String refreshTokenString){


        RefreshToken refreshToken = refreshTokenService.verifyRefreshToken(refreshTokenString);

        User user= refreshToken.getUser();

        UserDetailsImpl userDetails = UserDetailsImpl.build(user);


        ResponseCookie newJwtCookie = jwtService.generatejwtCookie(userDetails);
        String role = userDetails.getAuthorities().stream().findFirst().get().getAuthority();

        UserDTO userDTO = new UserDTO(user.getFirstName(), user.getLastName(), userDetails.getEmail(), USER_ROLE.valueOf(role));
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, newJwtCookie.toString()).body(userDTO);

    }


    @GetMapping("/me")
    public ResponseEntity<UserDTO> getMe(){
        UserDTO userDTO = authService.getCurrentUser();
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }


}
