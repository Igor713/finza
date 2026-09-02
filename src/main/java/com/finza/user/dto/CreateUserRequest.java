package com.finza.user.dto;

public record CreateUserRequest(
    String name,
    String email,
    String password
) {}
