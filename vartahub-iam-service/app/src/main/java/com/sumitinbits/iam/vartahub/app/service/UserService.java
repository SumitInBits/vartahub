package com.sumitinbits.iam.vartahub.app.service;

import com.sumitinbits.vartahub.iam.api.dto.OnboardUserRequest;
import com.sumitinbits.vartahub.iam.api.dto.OnboardingStatusDto;
import com.sumitinbits.vartahub.iam.api.dto.UserDto;

import java.util.UUID;

public interface UserService {
    UUID onboardUser(OnboardUserRequest onboardUserRequest);

    UserDto getUserByIdUnsafe(UUID userId);

    UserDto getUserByIdentityUnsafe(UUID identityId);

    UserDto getUser();

    OnboardingStatusDto getUserOnboardingStatus();
}
