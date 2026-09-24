package com.finza.workspace;

import com.finza.user.dto.UserResponse;
import com.finza.user.entity.User;
import com.finza.workspace.dto.WorkspaceRequest;
import com.finza.workspace.dto.WorkspaceResponse;
import com.finza.workspace.entity.Workspace;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

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

    public WorkspaceResponse update(UUID workspaceId, WorkspaceRequest request, User user) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new RuntimeException("Workspace not found"));

        if (!workspace.getCreatedBy().getId().equals(user.getId())) {
            throw new RuntimeException("User does not have permission");
        }

        workspace.setName(request.name());

        Workspace saved = workspaceRepository.save(workspace);

        return new WorkspaceResponse(
                saved.getName(),
                new UserResponse(
                        saved.getCreatedBy().getId(),
                        saved.getCreatedBy().getName(),
                        saved.getCreatedBy().getEmail()
                )
        );
    }

    public Page<WorkspaceResponse> findAll(Pageable pageable, User user) {
        return workspaceRepository
                .findByCreatedById(user.getId(), pageable)
                .map(workspace -> new WorkspaceResponse(
                        workspace.getName(),
                        new UserResponse(
                                workspace.getCreatedBy().getId(),
                                workspace.getCreatedBy().getName(),
                                workspace.getCreatedBy().getEmail()
                        )
                ));
    }

    public void delete(UUID workspaceId, User user) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new RuntimeException("Workspace not found"));

        workspaceRepository.delete(workspace);
    }
}
