package com.sumitinbits.iam.api.dto;

import com.sumitinbits.iam.api.enums.Experience;
import com.sumitinbits.iam.api.enums.Role;

import java.util.List;

public record UserRequest(
        String firstName,
        String lastName,
        String email,
        String username,
        String password,
        List<UserSpecialisationRequest> specialisations,
        Experience experience,
        Role role,
        Integer experienceYears,
        String organizationName
) {
}
