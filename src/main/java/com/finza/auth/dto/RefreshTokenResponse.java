package com.finza.auth.dto;

public record RefreshTokenResponse(
        String accessToken,
        long expiresIn
) {}