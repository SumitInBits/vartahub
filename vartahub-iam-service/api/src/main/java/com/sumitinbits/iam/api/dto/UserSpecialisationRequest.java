package com.sumitinbits.iam.api.dto;

import java.util.UUID;

public record UserSpecialisationRequest(
        UUID specialisationId,
        Integer proficiencyLevel
) {
}
