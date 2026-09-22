package com.finza.workspace;

import com.finza.user.UserRepository;
import com.finza.user.dto.UserResponse;
import com.finza.user.entity.User;
import com.finza.workspace.dto.WorkspaceRequest;
import com.finza.workspace.dto.WorkspaceResponse;
import com.finza.workspace.entity.Workspace;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class WorkspaceService {
    private final WorkspaceRepository workspaceRepository;
    private final UserRepository userRepository;

    public WorkspaceService(WorkspaceRepository workspaceRepository, UserRepository userRepository) {

        this.workspaceRepository = workspaceRepository;
        this.userRepository = userRepository;
    }

    public WorkspaceResponse create(WorkspaceRequest request, UUID userId) {
        Workspace workspace = new Workspace();
        System.out.print("userId ====> " + userId);

        User user = userRepository.findById(userId)
                .orElseThrow();

        workspace.setCreatedBy(user);

        workspace.setName(request.name());
        workspace.setCreatedBy(user);

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
}
