package com.sumitinbits.vartahub.iam.api.security;

import java.util.UUID;

public record AuthenticatedUser(
        UUID identityId
) {
}