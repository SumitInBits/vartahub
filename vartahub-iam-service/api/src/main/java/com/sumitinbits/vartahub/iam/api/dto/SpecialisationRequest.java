package com.sumitinbits.vartahub.iam.api.dto;

import jakarta.validation.constraints.NotEmpty;

public record SpecialisationRequest (
        @NotEmpty String name
){
}
