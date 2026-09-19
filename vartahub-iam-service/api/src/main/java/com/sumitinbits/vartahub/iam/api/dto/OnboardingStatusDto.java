package com.sumitinbits.vartahub.iam.api.dto;

import com.sumitinbits.vartahub.iam.api.enums.OnboardingStatus;

import java.util.UUID;

public record OnboardingStatusDto(
        UUID keycloakId,
        OnboardingStatus onboardingStatus
) {
}
