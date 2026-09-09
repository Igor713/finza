package com.finza.auth.service;

import com.finza.auth.entity.RefreshToken;
import com.finza.auth.repository.RefreshTokenRepository;
import com.finza.user.entity.User;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository repository;

    private final SecureRandom secureRandom = new SecureRandom();

    public RefreshTokenService(RefreshTokenRepository repository) {
        this.repository = repository;
    }

    public RefreshToken create(User user) {

        byte[] randomBytes = new byte[32];

        secureRandom.nextBytes(randomBytes);

        String token = Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(randomBytes);

        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setToken(token);
        refreshToken.setUser(user);
        refreshToken.setExpiresAt(
                LocalDateTime.now().plusDays(30)
        );
        refreshToken.setRevoked(false);

        return repository.save(refreshToken);
    }

    public RefreshToken validate(String token) {

        RefreshToken refreshToken = repository
                .findByToken(token)
                .orElseThrow(() ->
                        new RuntimeException("Refresh token inválido")
                );

        if (refreshToken.isRevoked()) {
            throw new RuntimeException("Refresh token revogado");
        }

        if (refreshToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Refresh token expirado");
        }

        return refreshToken;
    }

    public void revoke(RefreshToken refreshToken) {
        refreshToken.setRevoked(true);
        repository.save(refreshToken);
    }
}