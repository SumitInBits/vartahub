package com.sumitinbits.iam.securitycore.validator;

import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class JwtValidator {
    public void validate(Jwt jwt) {
        validateEmail(jwt);
        validateProfile(jwt);
    }

    private void validateEmail(Jwt jwt) {
        Boolean emailVerified = jwt.getClaimAsBoolean("email_verified");
        if (!Boolean.TRUE.equals(emailVerified)) {
            throw new OAuth2AuthenticationException(new OAuth2Error("email_not_verified"), "Email address is not verified");
        }
    }

    private void validateProfile(Jwt jwt) {
        Boolean profileEnabled = jwt.getClaimAsBoolean("profile_enabled");
        if (!Boolean.TRUE.equals(profileEnabled)) {
            throw new OAuth2AuthenticationException(new OAuth2Error("profile_not_enabled"), "User profile is not enabled");
        }
    }
}
