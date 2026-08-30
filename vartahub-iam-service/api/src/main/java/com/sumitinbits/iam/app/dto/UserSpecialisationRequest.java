package com.sumitinbits.iam.app.dto;

import java.util.UUID;

public record UserSpecialisationRequest(
        UUID id,
        Integer proficiencyLevel
) {
}
