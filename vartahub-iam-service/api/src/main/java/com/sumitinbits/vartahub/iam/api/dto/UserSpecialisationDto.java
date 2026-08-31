package com.sumitinbits.vartahub.iam.api.dto;

import com.sumitinbits.vartahub.iam.api.enums.Proficiency;

import java.util.UUID;

public record UserSpecialisationDto(
        UUID id,
        SpecialisationDto specialisation,
        Proficiency proficiency
) {
}
