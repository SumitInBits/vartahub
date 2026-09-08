package com.sumitinbits.vartahub.iam.api.security;

import java.util.UUID;

public record VartahubAuthUser(
        UUID keycloakId
) {
}