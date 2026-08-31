package com.sumitinbits.vartahub.iam.api.dto;

import com.sumitinbits.vartahub.iam.api.enums.Experience;

import java.util.List;

public record UserRequest(
        String firstName,
        String lastName,
        String email,
        String username,
        String password,
        List<UserSpecialisationRequest> specialisations,
        Experience experience,
        String role,
        Integer experienceYears,
        String organizationName
) {
}
