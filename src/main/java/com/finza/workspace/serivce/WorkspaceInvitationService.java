package com.finza.workspace.serivce;

import com.finza.user.entity.User;
import com.finza.workspace.dto.InviteRequest;
import com.finza.workspace.entity.Workspace;
import com.finza.workspace.entity.WorkspaceInvitation;
import com.finza.workspace.entity.WorkspaceMember;
import com.finza.workspace.enums.InviteStatus;
import com.finza.workspace.repository.WorkspaceInvitationRepository;
import com.finza.workspace.repository.WorkspaceMemberRepository;
import com.finza.workspace.repository.WorkspaceRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HexFormat;
import java.util.UUID;

@Service
public class WorkspaceInvitationService {
    private final WorkspaceInvitationRepository workspaceInvitationRepository;
    private final WorkspaceRepository workspaceRepository;
    private final WorkspaceMemberRepository workspaceMemberRepository;

    public WorkspaceInvitationService(
            WorkspaceInvitationRepository workspaceInvitationRepository,
            WorkspaceRepository workspaceRepository,
            WorkspaceMemberRepository workspaceMemberRepository
    ) {

        this.workspaceInvitationRepository = workspaceInvitationRepository;
        this.workspaceRepository = workspaceRepository;
        this.workspaceMemberRepository = workspaceMemberRepository;
    }

    @Transactional
    public void create(
            UUID workspaceId,
            InviteRequest request,
            User user
    ) {
        Workspace workspace = workspaceRepository
                .findById(workspaceId)
                .orElseThrow(() -> new RuntimeException("Workspace not found"));

        String token = generateToken();

        WorkspaceInvitation invite = new WorkspaceInvitation();

        invite.setWorkspace(workspace);
        invite.setEmail(request.email());
        invite.setRole(request.role());
        invite.setTokenHash(hashToken(token));
        invite.setExpiresAt(LocalDateTime.now().plusDays(2));
        invite.setStatus(InviteStatus.PENDING);

        workspaceInvitationRepository.save(invite);

//        TODO
//        emailService.sendInvite(
//                request.email(),
//                workspace.getName(),
//                token
//        );
    }

    private String generateToken() {
        byte[] bytes = new byte[32];

        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(bytes);

        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(bytes);
    }

    private String hashToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] hash = digest.digest(
                    token.getBytes(StandardCharsets.UTF_8)
            );

            return HexFormat.of().formatHex(hash);

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public void accept(String token, User user) {

        String tokenHash = hashToken(token);

        WorkspaceInvitation invite =
                workspaceInvitationRepository
                        .findByTokenHash(tokenHash)
                        .orElseThrow(() ->
                                new RuntimeException("Invalid invite"));

        if (invite.getStatus() != InviteStatus.PENDING) {
            throw new RuntimeException("Invite is no longer valid");
        }

        if (invite.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Invite expired");
        }

        if (!invite.getEmail().equalsIgnoreCase(user.getEmail())) {
            throw new RuntimeException("Invite belongs to another email");
        }

        WorkspaceMember member = new WorkspaceMember();

        member.setWorkspace(invite.getWorkspace());
        member.setUser(user);
        member.setRole(invite.getRole());

        workspaceMemberRepository.save(member);

        invite.setStatus(InviteStatus.ACCEPTED);
        invite.setAcceptedAt(LocalDateTime.now());
    }
}
