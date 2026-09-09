package com.finza.auth.service;

import com.finza.auth.dto.LoginRequest;
import com.finza.auth.dto.LoginResponse;
import com.finza.auth.dto.RefreshTokenResponse;
import com.finza.auth.dto.RegisterRequest;
import com.finza.auth.entity.RefreshToken;
import com.finza.user.entity.User;
import com.finza.user.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            RefreshTokenService refreshTokenService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }

    public void register(RegisterRequest request) {

        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new RuntimeException("Email já cadastrado");
        }

        User user = new User();

        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(
                passwordEncoder.encode(request.password())
        );
        user.setRole("USER");
        user.setActive(true);

        userRepository.save(user);
    }

    public LoginResponse login(LoginRequest request) {

        User user = userRepository
                .findByEmail(request.email())
                .orElseThrow(() ->
                        new RuntimeException("Email ou senha inválidos")
                );

        if (!user.isActive()) {
            throw new RuntimeException("Usuário desativado");
        }

        boolean passwordMatches = passwordEncoder.matches(
                request.password(),
                user.getPassword()
        );

        if (!passwordMatches) {
            throw new RuntimeException("Email ou senha inválidos");
        }

        String accessToken =
                jwtService.generateAccessToken(user);

        RefreshToken refreshToken =
                refreshTokenService.create(user);

        return new LoginResponse(
                accessToken,
                refreshToken.getToken(),
                900
        );
    }

    public RefreshTokenResponse refresh(String token) {

        RefreshToken refreshToken =
                refreshTokenService.validate(token);

        User user = refreshToken.getUser();

        String accessToken =
                jwtService.generateAccessToken(user);

        return new RefreshTokenResponse(
                accessToken,
                900
        );
    }

    public void logout(String token) {

        RefreshToken refreshToken =
                refreshTokenService.validate(token);

        refreshTokenService.revoke(refreshToken);
    }
}