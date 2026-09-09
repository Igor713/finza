package com.finza.auth.service;

import com.finza.user.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    private final SecretKey secretKey;

    public JwtService(@Value("${jwt.secret}") String secret) {
        this.secretKey = Keys.hmacShaKeyFor(
                java.util.Base64.getDecoder().decode(secret)
        );
    }

    public String generateAccessToken(User user) {

        Date now = new Date();

        Date expiration = new Date(
                now.getTime() + 15 * 60 * 1000
        );

        return Jwts.builder()
                .subject(user.getId().toString())
                .claim("email", user.getEmail())
                .claim("role", user.getRole())
                .issuedAt(now)
                .expiration(expiration)
                .signWith(secretKey)
                .compact();
    }

    public SecretKey getSecretKey() {
        return secretKey;
    }
}