package com.abdellah.spring_auth_starter.security.services;


import com.abdellah.spring_auth_starter.entity.RefreshToken;
import com.abdellah.spring_auth_starter.entity.User;
import com.abdellah.spring_auth_starter.repository.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;


@Service

public class RefreshTokenService {


    @Value("${spring.refreshtoken.expiration}")
    private  Long RefreshTokenExpirationMS;

    private final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";
    private final String REFRESH_TOKEN_COOKIE_PATH = "/";


    private RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Transactional
    public RefreshToken createRefreshToken(User user){

//        delete the old refresh token
        refreshTokenRepository.deleteByUser(user);


        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setExpiresAt(Instant.now().plusMillis(RefreshTokenExpirationMS));
        refreshToken.setToken(UUID.randomUUID().toString());


        refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    public boolean isRefreshTokenExpired(RefreshToken token) {
        return token.getExpiresAt().isBefore(Instant.now());
    }

    public ResponseCookie getRefreshTokenCookie(RefreshToken token){
        ResponseCookie refreshTokenCookie = ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME, token.getToken())
                .maxAge(RefreshTokenExpirationMS / 1000)
                .path(REFRESH_TOKEN_COOKIE_PATH)
                .httpOnly(true)
                .secure(false)
                .sameSite("lax")
                .build();

        return refreshTokenCookie;
    }

    public ResponseCookie getCleanRefreshTokenCookie(){
        return ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME, null).maxAge(0).path(REFRESH_TOKEN_COOKIE_PATH).build();
    }


    public RefreshToken verifyRefreshToken(String tokenString){
        RefreshToken refreshToken = refreshTokenRepository.findByToken(tokenString).orElseThrow( ()-> new RuntimeException("RefreshToken not found"));

        if(isRefreshTokenExpired(refreshToken)){
            refreshTokenRepository.delete(refreshToken);
            throw new RuntimeException("RefreshToken is expired");
        }

        return refreshToken;
    }

    public void deleteByUser(User user){
        refreshTokenRepository.deleteByUser(user);
    }

    @Transactional
    public void deleteByToken(String refreshToken){
        refreshTokenRepository.findByToken(refreshToken).ifPresent(refreshTokenRepository::delete);
    }

}
