package com.sumitinbits.iam.vartahub.app.service.impl;

import com.sumitinbits.iam.vartahub.app.config.KeycloakConfig;
import com.sumitinbits.vartahub.iam.api.dto.UserRequest;
import com.sumitinbits.iam.vartahub.app.service.IdentityProviderService;
import com.sumitinbits.vartahub.iam.api.enums.Role;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class KeycloakIdentityProviderServiceImpl implements IdentityProviderService {

    private final Keycloak keycloak;
    private final KeycloakConfig keycloakConfig;

    @Override
    public UUID createUser(UserRequest request,  List<Role> roles) {
        UserRepresentation user = constructUserRepresentation(request);

        try (Response response = getRealmResource().users().create(user)) {
            if (!response.getStatusInfo().getFamily().equals(Response.Status.Family.SUCCESSFUL)) {
                log.error("Failed to create Keycloak user. Status: {}, Email: {}", response.getStatus(), request.email());
                throw new IllegalStateException("Failed to create user in Keycloak. Status: " + response.getStatus());
            }

            UUID userId = UUID.fromString(CreatedResponseUtil.getCreatedId(response));
            log.info("Keycloak user created successfully. UserId: {}", userId);
            assignRealmRole(userId, roles);
            return userId;
        }
    }

    @Override
    public UserRepresentation getUser(UUID keycloakUserId) {
        return getUserResource(keycloakUserId).toRepresentation();
    }

    @Override
    public void updateUser(UUID keycloakUserId, UserRequest request) {
        UserResource userResource = getUserResource(keycloakUserId);
        UserRepresentation user = userResource.toRepresentation();

        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setEmail(request.email());
        user.setUsername(request.username());

        userResource.update(user);

        if (request.password() != null && !request.password().isBlank()) {
            updatePassword(userResource, request.password());
        }

        log.info("Keycloak user updated successfully. UserId: {}", keycloakUserId);
    }

    @Override
    public void deleteUser(UUID userId) {
        try {
            getUserResource(userId).remove();
            log.info("Keycloak user deleted successfully. UserId: {}", userId);
        } catch (Exception e) {
            log.error("Failed to delete Keycloak user. UserId: {}", userId, e);
            throw new IllegalStateException("Failed to delete user in Keycloak. UserId: " + userId, e);
        }
    }

    @Override
    public void enableUser(UUID keycloakUserId) {
        updateUserStatus(keycloakUserId, true);
    }

    @Override
    public void disableUser(UUID keycloakUserId) {
        updateUserStatus(keycloakUserId, false);
    }

    private void updateUserStatus(UUID keycloakUserId, boolean enabled) {
        UserRepresentation user = getUserResource(keycloakUserId).toRepresentation();
        user.setEnabled(enabled);
        getUserResource(keycloakUserId).update(user);
        log.info("Keycloak user {} successfully. UserId: {}", enabled ? "enabled" : "disabled", keycloakUserId);
    }

    private void updatePassword(UserResource userResource, String password) {
        CredentialRepresentation credential = createPasswordCredential(password);
        userResource.resetPassword(credential);
    }

    private UserRepresentation constructUserRepresentation(UserRequest request) {
        UserRepresentation user = new UserRepresentation();

        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setEmail(request.email());
        user.setUsername(request.username());
        user.setEnabled(true);
        user.setEmailVerified(true);
        user.setCredentials(List.of(createPasswordCredential(request.password())));
        return user;
    }

    private CredentialRepresentation createPasswordCredential(String password) {
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        credential.setTemporary(false);
        return credential;
    }

    private RealmResource getRealmResource() {
        return keycloak.realm(keycloakConfig.getRealm());
    }

    private UserResource getUserResource(UUID userId) {
        return getRealmResource().users().get(userId.toString());
    }

    private void assignRealmRole(UUID userId, List<Role> roles) {
        RealmResource realm = getRealmResource();

        List<RoleRepresentation> roleRepresentations = roles.stream()
                .map(role -> realm.roles()
                        .get(role.name())
                        .toRepresentation()
                )
                .toList();

        getUserResource(userId).roles().realmLevel().add(roleRepresentations);
        log.info("Assigned realm role '{}' to user {}", roles, userId);
    }
}