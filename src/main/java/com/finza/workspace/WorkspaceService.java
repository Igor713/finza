package com.finza.workspace;

import com.finza.user.dto.UserResponse;
import com.finza.user.entity.User;
import com.finza.workspace.dto.WorkspaceRequest;
import com.finza.workspace.dto.WorkspaceResponse;
import com.finza.workspace.entity.Workspace;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class WorkspaceService {
    private final WorkspaceRepository workspaceRepository;

    public WorkspaceService(WorkspaceRepository workspaceRepository) {
        this.workspaceRepository = workspaceRepository;
    }

    public WorkspaceResponse create(WorkspaceRequest request, User user) {
        Workspace workspace = new Workspace();

        workspace.setCreatedBy(user);

        workspace.setName(request.name());

        Workspace savedWorkspace = workspaceRepository.save(workspace);

        return new WorkspaceResponse(
                savedWorkspace.getName(),
                new UserResponse(
                        user.getId(),
                        user.getName(),
                        user.getEmail()
                )
        );
    }

    public Page<WorkspaceResponse> findAll(Pageable pageable) {
        return workspaceRepository
                .findAll(pageable)
                .map(workspace -> new WorkspaceResponse(
                        workspace.getName(),
                        new UserResponse(
                                workspace.getCreatedBy().getId(),
                                workspace.getCreatedBy().getName(),
                                workspace.getCreatedBy().getEmail()
                        )
                ));
    }
}
