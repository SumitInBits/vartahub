package com.sumitinbits.iam.vartahub.app.service;

import com.sumitinbits.vartahub.iam.api.dto.UserRequest;
import com.sumitinbits.vartahub.iam.api.enums.Role;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;
import java.util.UUID;

public interface IdentityProviderService {
    UUID createUser(UserRequest userRequest, List<Role> roles);

    UserRepresentation getUser(UUID identityProviderId);

    void updateUser(UUID identityProviderId, UserRequest request);

    void deleteUser(UUID userId);

    void enableUser(UUID identityProviderId);

    void disableUser(UUID identityProviderId);
}
