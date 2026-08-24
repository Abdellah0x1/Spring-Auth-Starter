package com.abdellah.spring_auth_starter.security.services;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Service
public class JwtService {
    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

    @Value("${spring.jwt.secret}")
    private String jwtSecret;

    @Value("${spring.jwt.expiration}")
    private int jwtExpirationInMs;



    public String generateJwtTokenFromEmail(String email){
        return Jwts.builder().subject(email).issuedAt(new Date()).expiration(new Date(new Date().getTime() + jwtExpirationInMs)).signWith(key()).compact();
    }

    private Key key(){
        return  Keys.hmacShaKeyFor(
                Decoders.BASE64URL.decode(jwtSecret)
        );
    }

    public boolean validateJwtToken(String token){
        try {
            logger.debug("Validating jwt token...");
            Jwts.parser()
                    .verifyWith((SecretKey) key())
                    .build().parseSignedClaims(token);
            return true;

        }catch(MalformedJwtException e){
            logger.error("Invalid JWT token : {}", e.getMessage());

        }catch (ExpiredJwtException e){
            logger.error("Expired JWT token: {}", e.getMessage());
        }catch (UnsupportedJwtException e){
            logger.error("Unsupported JWT token: {}", e.getMessage());
        } catch(IllegalArgumentException e){
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }


}
