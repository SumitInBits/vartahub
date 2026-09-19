package com.sumitinbits.iam.vartahub.app.service;

import com.sumitinbits.vartahub.iam.api.dto.OnboardUserRequest;
import com.sumitinbits.vartahub.iam.api.dto.OnboardingStatusDto;
import com.sumitinbits.vartahub.iam.api.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;
import java.util.UUID;

public interface UserService {
    UUID onboardUser(OnboardUserRequest onboardUserRequest);

    UserDto getUserByIdUnsafe(UUID userId);

    UserDto getUserByIdentityUnsafe(UUID identityId);

    UserDto getUser();

    OnboardingStatusDto getUserOnboardingStatus();

    Page<UserDto> getInstructors(Set<UUID> specialisationIds, Integer minExperienceYears, Pageable pageable);
}
