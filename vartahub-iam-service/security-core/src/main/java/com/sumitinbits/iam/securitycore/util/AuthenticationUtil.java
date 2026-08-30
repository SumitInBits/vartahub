package com.sumitinbits.iam.securitycore.util;

import com.sumitinbits.iam.api.security.AuthenticatedUser;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Objects;
import java.util.UUID;

@UtilityClass
public class AuthenticationUtil {
    public AuthenticatedUser getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof JwtAuthenticationToken jwtAuthentication)) {
            throw new IllegalStateException("No authenticated user found");
        }

        Jwt token = jwtAuthentication.getToken();
        UUID identifyProviderId = UUID.fromString(Objects.requireNonNull(token.getSubject()));
        return new AuthenticatedUser(identifyProviderId);
    }
}
