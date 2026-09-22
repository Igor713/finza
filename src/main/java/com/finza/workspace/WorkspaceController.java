package com.finza.workspace;

import com.finza.config.CurrentUser;
import com.finza.user.entity.User;
import com.finza.workspace.dto.WorkspaceRequest;
import com.finza.workspace.dto.WorkspaceResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

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
        System.out.println("====>" + user.getName());
        return workspaceService.create(request, user);
    }

    @GetMapping
    public Page<WorkspaceResponse> findAll(Pageable pageable) {
        return workspaceService.findAll(pageable);
    }
}