package com.sumitinbits.iam.vartahub.app.service;

import com.sumitinbits.vartahub.iam.api.enums.Role;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;
import java.util.UUID;

public interface KeycloakService {
    UserRepresentation getUser(UUID identityProviderId);

    void updateUser(UUID keycloakUserId, UUID vartahubUserId, List<Role> roles);

    void deleteUser(UUID userId);

    void enableUser(UUID identityProviderId);

    void disableUser(UUID identityProviderId);

    void assignRealmRole(UUID userId, List<Role> roles);
}
