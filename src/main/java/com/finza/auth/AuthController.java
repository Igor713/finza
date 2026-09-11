package com.finza.auth;

import com.finza.auth.dto.*;
import com.finza.auth.service.AuthService;
import com.finza.auth.service.JwtService;
import com.finza.auth.service.RefreshTokenService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public AuthController(
            AuthService authService,
            JwtService jwtService,
            RefreshTokenService refreshTokenService
    ) {
        this.authService = authService;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/register")
    public void register(
            @RequestBody RegisterRequest request
    ) {
        authService.register(request);
    }

    @PostMapping("/login")
    public LoginResponse login(
            @RequestBody LoginRequest request
    ) {
        return authService.login(request);
    }

    @PostMapping("/refresh")
    public RefreshTokenResponse refresh(
            @RequestBody RefreshTokenRequest request
    ) {
        return authService.refresh(
                request.refreshToken()
        );
    }

    @PostMapping("/logout")
    public void logout(
            @RequestBody RefreshTokenRequest request
    ) {
        authService.logout(
                request.refreshToken()
        );
    }
}