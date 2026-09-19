package com.sumitinbits.iam.vartahub.app.service.impl;

import com.sumitinbits.iam.vartahub.app.config.KeycloakConfig;
import com.sumitinbits.iam.vartahub.app.service.KeycloakService;
import com.sumitinbits.vartahub.iam.api.enums.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class KeycloakServiceImpl implements KeycloakService {
    private final Keycloak keycloak;
    private final KeycloakConfig keycloakConfig;

    @Override
    public UserRepresentation getUser(UUID keycloakUserId) {
        return getUserResource(keycloakUserId).toRepresentation();
    }

    @Override
    public void updateUser(UUID keycloakUserId, UUID vartahubUserId, List<Role> roles) {
        UserResource userResource = getUserResource(keycloakUserId);
        UserRepresentation user = userResource.toRepresentation();

        Map<String, List<String>> attributes = user.getAttributes();
        if(attributes == null) {
            attributes = new HashMap<>();
        }
        attributes.put("vartahubUserId", List.of(vartahubUserId.toString()));

        user.setAttributes(attributes);

        List<String> keycloakRoles = roles.stream().map(role -> role.name().toLowerCase()).toList();

        user.setRealmRoles(keycloakRoles);
        userResource.update(user);

        log.info(
                "Keycloak user updated successfully. KeycloakUserId: {}, VartahubUserId: {}",
                keycloakUserId,
                vartahubUserId
        );
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

    @Override
    public void assignRealmRole(UUID userId, List<Role> roles) {
        RealmResource realm = getRealmResource();

        List<RoleRepresentation> roleRepresentations = roles.stream()
                .map(role -> realm.roles()
                        .get(role.name().toLowerCase())
                        .toRepresentation()
                )
                .toList();

        getUserResource(userId).roles().realmLevel().add(roleRepresentations);
        log.info("Assigned realm role '{}' to user {}", roles, userId);
    }

    private void updateUserStatus(UUID keycloakUserId, boolean enabled) {
        UserRepresentation user = getUserResource(keycloakUserId).toRepresentation();
        user.setEnabled(enabled);
        getUserResource(keycloakUserId).update(user);
        log.info("Keycloak user {} successfully. UserId: {}", enabled ? "enabled" : "disabled", keycloakUserId);
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

}