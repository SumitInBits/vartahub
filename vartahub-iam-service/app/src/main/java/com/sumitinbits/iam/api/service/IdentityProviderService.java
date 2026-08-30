package com.sumitinbits.iam.api.service;

import com.sumitinbits.iam.api.dto.UserRequest;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.UUID;

public interface IdentityProviderService {
    UUID createUser(UserRequest userRequest);

    UserRepresentation getUser(UUID identityProviderId);

    void updateUser(UUID identityProviderId, UserRequest request);

    void deleteUser(UUID userId);

    void enableUser(UUID identityProviderId);

    void disableUser(UUID identityProviderId);
}
