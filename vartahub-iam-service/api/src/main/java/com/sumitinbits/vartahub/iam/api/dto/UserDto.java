package com.sumitinbits.vartahub.iam.api.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.sumitinbits.vartahub.iam.api.enums.Experience;
import com.sumitinbits.vartahub.iam.api.enums.OnboardingStatus;
import com.sumitinbits.vartahub.iam.api.enums.Role;

import java.util.List;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserDto(
        UUID id,
        String firstName,
        String lastName,
        String username,
        String email,
        String profilePhotoKey,
        UUID identityProviderId,
        Experience experience,
        OnboardingStatus onboardingStatus,
        Role role,
        Integer experienceYears,
        String organizationName,
        String organizationRole,
        List<UserSpecialisationDto> userSpecialisations
) {
}