package com.sumitinbits.vartahub.iam.api.dto;

import com.sumitinbits.vartahub.iam.api.enums.Proficiency;

import java.util.UUID;

public record UserSpecialisationRequest(
        UUID specialisationId,
        Proficiency proficiency
) {
}
