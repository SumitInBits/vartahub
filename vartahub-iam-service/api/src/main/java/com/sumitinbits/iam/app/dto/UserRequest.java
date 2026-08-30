package com.sumitinbits.iam.app.dto;

import com.sumitinbits.iam.app.annotations.AllowedRoles;
import com.sumitinbits.iam.app.enums.Experience;
import com.sumitinbits.iam.app.enums.Role;
import jakarta.validation.constraints.*;

import java.util.List;

public record UserRequest(
        @NotBlank
        String firstName,

        @NotBlank
        String lastName,

        @Email
        String email,

        @NotBlank
        String username,

        @Min(8) @Max(24)
        String password,

        @NotEmpty
        List<UserSpecialisationRequest> specialisations,

        Experience experience,

        @AllowedRoles(
                value = {Role.USER, Role.INSTRUCTOR},
                message = "Role must be USER or INSTRUCTOR"
        )
        Role role,

        Integer experienceYears,

        String organizationName
) {
}
