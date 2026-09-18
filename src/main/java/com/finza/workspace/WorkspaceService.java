package com.finza.workspace;

import com.finza.user.entity.User;
import com.finza.workspace.dto.WorkspaceRequest;
import com.finza.workspace.entity.Workspace;
import org.springframework.stereotype.Service;

@Service
public class WorkspaceService {
    private final WorkspaceRepository workspaceRepository;

    public WorkspaceService(WorkspaceRepository workspaceRepository) {
        this.workspaceRepository = workspaceRepository;
    }

    public Workspace create(WorkspaceRequest request, User user) {
        Workspace workspace = new Workspace();

        workspace.setName(request.name());
        workspace.setCreatedBy(user);

        return workspaceRepository.save(workspace);
    }
}
