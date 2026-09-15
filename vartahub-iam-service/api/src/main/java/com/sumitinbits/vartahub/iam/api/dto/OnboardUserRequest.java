package com.sumitinbits.vartahub.iam.api.dto;

import com.sumitinbits.vartahub.iam.api.enums.Experience;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record OnboardUserRequest(
        @NotEmpty List<UserSpecialisationRequest> specialisations,
        @NotNull Experience experience,
        @NotNull String role,
        Integer experienceYears,
        String organizationName,
        String organizationRole
) {
}
