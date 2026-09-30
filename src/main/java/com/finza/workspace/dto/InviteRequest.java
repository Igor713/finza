package com.finza.workspace.dto;

import com.finza.workspace.enums.WorkspaceRole;

public record InviteRequest(
        String email,
        WorkspaceRole role
) {}