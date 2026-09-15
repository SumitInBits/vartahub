package com.sumitinbits.iam.vartahub.app.service;

import com.sumitinbits.vartahub.iam.api.dto.OnboardUserRequest;
import com.sumitinbits.vartahub.iam.api.dto.UserDto;
import com.sumitinbits.vartahub.iam.api.enums.OnboardingStatus;

import java.util.UUID;

public interface UserService {
    UUID onboardUser(OnboardUserRequest onboardUserRequest);

    UserDto getUserByIdUnsafe(UUID userId);

    UserDto getUserByIdentityUnsafe(UUID identityId);

    UserDto getUser();

    OnboardingStatus getUserOnboardingStatus();
}
