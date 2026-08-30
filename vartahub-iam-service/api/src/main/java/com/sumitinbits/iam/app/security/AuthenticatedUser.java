package com.sumitinbits.iam.app.security;

import java.util.UUID;

public record AuthenticatedUser(
        UUID identityProviderId
) {
}