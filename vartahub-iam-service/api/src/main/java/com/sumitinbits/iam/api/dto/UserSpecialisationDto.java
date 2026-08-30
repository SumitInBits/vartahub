package com.sumitinbits.iam.api.dto;

import java.util.List;
import java.util.UUID;

public record UserSpecialisationDto(
        UUID id,
        List<SpecialisationDto> specialisations,
        Integer proficiencyLevel
) {
}
