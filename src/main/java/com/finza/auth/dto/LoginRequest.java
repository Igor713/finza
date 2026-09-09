package com.finza.auth.dto;

public record LoginRequest(
        String email,
        String password
) {}