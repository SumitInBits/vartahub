package com.sumitinbits.iam.api.security;

import java.util.UUID;

public record AuthenticatedUser(
        UUID identityProviderId
) {
}