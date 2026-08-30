package com.sumitinbits.iam.app.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record SpecialisationRequest (
        @NotNull
        String name,
        UUID parentId
        ){
}
