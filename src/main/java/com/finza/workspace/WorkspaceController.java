package com.finza.workspace;

import com.finza.config.CurrentUser;
import com.finza.user.entity.User;
import com.finza.workspace.dto.InviteRequest;
import com.finza.workspace.dto.WorkspaceRequest;
import com.finza.workspace.dto.WorkspaceResponse;
import com.finza.workspace.serivce.WorkspaceInvitationService;
import com.finza.workspace.serivce.WorkspaceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/workspace")
public class WorkspaceController {

    private final WorkspaceService workspaceService;
    private final WorkspaceInvitationService workspaceInvitationService

    public WorkspaceController(WorkspaceService workspaceService,
                               WorkspaceInvitationService workspaceInvitationService) {

        this.workspaceService = workspaceService;
        this.workspaceInvitationService = workspaceInvitationService;
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
    public void delete(@PathVariable UUID id, @CurrentUser User user) {
        workspaceService.delete(id, user);
    }

    @PostMapping("/{workspaceId}/invites")
    public void invite(
            @PathVariable UUID workspaceId,
            @RequestBody InviteRequest request,
            @CurrentUser User user
    ) {
        workspaceInvitationService.create(
                workspaceId,
                request,
                user
        );
    }
}