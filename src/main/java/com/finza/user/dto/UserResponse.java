package com.finza.user.dto;

import lombok.Getter;

import java.util.UUID;

public record UserResponse(
        UUID id,
        String name,
        String email
) {}