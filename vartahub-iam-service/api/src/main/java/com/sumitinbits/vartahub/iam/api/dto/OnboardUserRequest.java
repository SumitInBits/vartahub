package com.sumitinbits.vartahub.iam.api.dto;

import com.sumitinbits.vartahub.iam.api.enums.Experience;
import com.sumitinbits.vartahub.iam.api.enums.Role;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record OnboardUserRequest(
        @NotEmpty List<UserSpecialisationRequest> specialisations,
        @NotNull Experience experience,
        @NotNull Role role,
        Integer experienceYears,
        String organizationName,
        String organizationRole
) {
    @AssertTrue(message = "Unsupported role")
    public boolean isValidRole() { return role == null || role != Role.ADMIN; }
}
