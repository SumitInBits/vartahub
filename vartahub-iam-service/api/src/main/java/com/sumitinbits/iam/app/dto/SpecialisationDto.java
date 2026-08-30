package com.sumitinbits.iam.app.dto;

import java.util.List;
import java.util.UUID;

public record SpecialisationDto(
         UUID id,
         String name,
         String slug,
         Integer depth,
         List<SpecialisationDto> children
) {
}
