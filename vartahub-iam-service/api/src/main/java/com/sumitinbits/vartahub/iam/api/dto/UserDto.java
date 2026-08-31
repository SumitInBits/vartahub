package com.sumitinbits.vartahub.iam.api.dto;


import com.sumitinbits.vartahub.iam.api.enums.Experience;
import com.sumitinbits.vartahub.iam.api.enums.Role;

import java.util.List;
import java.util.UUID;

public record UserDto(
        UUID id,
        String firstName,
        String lastName,
        String username,
        String email,
        String profilePhotoKey,
        UUID identityProviderId,
        Role role,
        Experience experience,
        Integer experienceYears,
        String organizationName,
        List<UserSpecialisationDto> userSpecialisations
) {
}