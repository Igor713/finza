package com.finza.workspace;

import com.finza.config.CurrentUser;
import com.finza.user.entity.User;
import com.finza.workspace.dto.WorkspaceRequest;
import com.finza.workspace.dto.WorkspaceResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
            @CurrentUser User user) {
        return workspaceService.create(request, user);
    }

    @PatchMapping("/{id}")
    public WorkspaceResponse update(
            @PathVariable UUID id,
            @RequestBody WorkspaceRequest request,
            @CurrentUser User user
    ) {
        return workspaceService.update(id, request, user);
    }

    @GetMapping
    public Page<WorkspaceResponse> findAll(@CurrentUser User user, Pageable pageable) {
        return workspaceService.findAll(pageable, user);
    }

    @DeleteMapping("/{id}")
    public WorkspaceResponse delete(@PathVariable UUID id, @CurrentUser User user) {
        return workspaceService.delete(id, user);
    }
}