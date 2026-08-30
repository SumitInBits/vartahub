package com.sumitinbits.iam.api.dto;

import java.util.UUID;

public record SpecialisationDto(
         UUID id,
         String name,
         String slug
) {
}
