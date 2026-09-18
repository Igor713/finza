package com.finza.workspace;

import com.finza.user.entity.User;
import com.finza.workspace.dto.WorkspaceRequest;
import com.finza.workspace.entity.Workspace;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/workspace")
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    public WorkspaceController(WorkspaceService workspaceService) {

        this.workspaceService = workspaceService;
    }

    @PostMapping
    public Workspace create(
            @RequestBody WorkspaceRequest request,
            @AuthenticationPrincipal User user) {
        return this.workspaceService.create(request, user);
    }
}
