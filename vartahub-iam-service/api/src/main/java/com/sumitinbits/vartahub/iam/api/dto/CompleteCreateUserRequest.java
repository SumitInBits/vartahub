package com.sumitinbits.vartahub.iam.api.dto;

import com.sumitinbits.vartahub.iam.api.enums.Experience;

import java.util.List;

public record CompleteCreateUserRequest(
        List<UserSpecialisationRequest> specialisations,
        Experience experience,
        String role,
        Integer experienceYears,
        String organizationName
) {
}
