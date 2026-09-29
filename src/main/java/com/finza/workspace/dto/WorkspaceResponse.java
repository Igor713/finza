package com.finza.workspace.dto;

import com.finza.user.dto.UserResponse;

public record WorkspaceResponse(
        String name,
        UserResponse createdBy
) {}