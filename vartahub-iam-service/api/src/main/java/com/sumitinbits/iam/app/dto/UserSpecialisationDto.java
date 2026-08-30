package com.sumitinbits.iam.app.dto;

import java.util.UUID;

public record UserSpecialisationDto(
        UUID id,
        SpecialisationDto specialisationDto,
        Integer proficiencyLevel
) {
}
