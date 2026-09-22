package com.finza.workspace;

import com.finza.workspace.dto.WorkspaceRequest;
import com.finza.workspace.dto.WorkspaceResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/workspace")
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    public WorkspaceController(WorkspaceService workspaceService) {
        this.workspaceService = workspaceService;
    }

    @PostMapping
    public WorkspaceResponse create(
            @RequestBody WorkspaceRequest request,
            @AuthenticationPrincipal Jwt jwt) {

        String subject = jwt.getSubject();

        if (subject == null) {
            throw new IllegalStateException("JWT subject is missing");
        }

        UUID userId = UUID.fromString(subject);

        return workspaceService.create(request, userId);
    }
}