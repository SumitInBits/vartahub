package com.sumitinbits.iam.vartahub.app.service.impl;

import com.sumitinbits.iam.vartahub.app.client.MeetingServiceClient;
import com.sumitinbits.iam.vartahub.app.mapper.UserMapper;
import com.sumitinbits.iam.vartahub.app.model.BaseEntity;
import com.sumitinbits.iam.vartahub.app.model.SpecialisationDbo;
import com.sumitinbits.iam.vartahub.app.model.UserDbo;
import com.sumitinbits.iam.vartahub.app.model.UserSpecialisationDbo;
import com.sumitinbits.iam.vartahub.app.repository.UserRepository;
import com.sumitinbits.iam.vartahub.app.service.KeycloakService;
import com.sumitinbits.iam.vartahub.app.service.SpecialisationService;
import com.sumitinbits.iam.vartahub.app.service.UserService;
import com.sumitinbits.vartahub.commons.dto.MeetingSummaryDto;
import com.sumitinbits.vartahub.commons.exception.OperationNotPermitted;
import com.sumitinbits.vartahub.commons.exception.ResourceNotFound;
import com.sumitinbits.vartahub.iam.api.dto.OnboardUserRequest;
import com.sumitinbits.vartahub.iam.api.dto.OnboardingStatusDto;
import com.sumitinbits.vartahub.iam.api.dto.UserDto;
import com.sumitinbits.vartahub.iam.api.dto.UserSpecialisationRequest;
import com.sumitinbits.vartahub.iam.api.enums.OnboardingStatus;
import com.sumitinbits.vartahub.iam.api.enums.Role;
import com.sumitinbits.vartahub.iam.securitycore.util.AuthenticationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final SpecialisationService specialisationService;
    private final MeetingServiceClient meetingServiceClient;
    private final KeycloakService identityService;

    @Transactional
    public UUID onboardUser(OnboardUserRequest onboardUserRequest) {
        Set<UUID> requestSpecialisationIds = onboardUserRequest.specialisations().stream()
                .map(UserSpecialisationRequest::specialisationId)
                .collect(Collectors.toSet());

        Map<UUID, SpecialisationDbo> specialisationDbos =
                specialisationService.getSpecialisationsDbosByIds(requestSpecialisationIds).stream()
                .collect(Collectors.toMap(SpecialisationDbo::getId, Function.identity()));

        if(requestSpecialisationIds.size() != specialisationDbos.size()) {
            throw new OperationNotPermitted("Some specialisations not found");
        }

        UUID keycloakId = AuthenticationUtil.getAuthenticatedUser().keycloakId();
        Optional<UserDbo> userDbo = userRepository.findByKeycloakId(keycloakId);

        if(userDbo.isPresent() && userDbo.get().getStatus() == OnboardingStatus.COMPLETED) {
            throw new OperationNotPermitted("User already completed onboarding");
        }

        UserRepresentation userRepresentation = identityService.getUser(keycloakId);
        UserDbo onboardingUser = UserDbo.builder()
                    .firstName(userRepresentation.getFirstName())
                    .lastName(userRepresentation.getLastName())
                    .email(userRepresentation.getEmail())
                    .username(userRepresentation.getUsername())
                    .keycloakId(UUID.fromString(userRepresentation.getId()))
                    .status(OnboardingStatus.PENDING)
                    .build();

        List<UserSpecialisationDbo> userSpecialisations = onboardUserRequest.specialisations().stream()
                .map(userSpecialisation -> new UserSpecialisationDbo(
                        onboardingUser,
                        specialisationDbos.get(userSpecialisation.specialisationId()),
                        userSpecialisation.proficiency()
                )).toList();

        identityService.assignRealmRole(keycloakId, List.of(onboardUserRequest.role()));
        onboardingUser.setUserSpecialisations(userSpecialisations);
        onboardingUser.setOrganizationRole(onboardUserRequest.organizationRole());
        onboardingUser.setExperience(onboardUserRequest.experience());
        onboardingUser.setExperienceYears(onboardUserRequest.experienceYears());
        onboardingUser.setOrganizationName(onboardUserRequest.organizationName());
        onboardingUser.setKeycloakId(keycloakId);
        onboardingUser.setRole(onboardUserRequest.role());
        onboardingUser.setStatus(OnboardingStatus.COMPLETED);
        return userRepository.save(onboardingUser).getId();
    }

    @Override
    public UserDto getUserByIdUnsafe(UUID userId) {
        UserDbo userDbo = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFound("User not found " + userId));
        return userMapper.toDto(userDbo);
    }

    @Override
    public UserDto getUserByIdentityUnsafe(UUID keycloakId) {
        UserDbo userDbo = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new ResourceNotFound("User not found " + keycloakId));
        return userMapper.toDto(userDbo);
    }

    @Override
    public UserDto getUser() {
        UUID keycloakId = AuthenticationUtil.getAuthenticatedUser().keycloakId();
        UserDbo userDbo = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new ResourceNotFound("User not found"));
        return userMapper.toDto(userDbo);
    }

    @Override
    public OnboardingStatusDto getUserOnboardingStatus() {
        UUID keycloakId = AuthenticationUtil.getAuthenticatedUser().keycloakId();
        OnboardingStatus onboardingStatus = userRepository.findOnboardingStatusByKeycloakId(keycloakId)
                .orElse(OnboardingStatus.PENDING);

        return new OnboardingStatusDto(keycloakId, onboardingStatus);
    }

    @Override
    public Page<UserDto> getInstructors(Set<UUID> specialisationIds, Integer minExperienceYears, Pageable pageable) {
        Page<UserDbo> userDbos = userRepository.findUsersByFilters(Role.INSTRUCTOR, specialisationIds, minExperienceYears, pageable);
        Set<UUID> userIds = userDbos.stream().map(BaseEntity::getId).collect(Collectors.toSet());
        try {
            Map<UUID, MeetingSummaryDto> userMeetingSummary = meetingServiceClient.getUserMeetingSummary(userIds);
            return userDbos.map(userDbo -> userMapper.toDto(userDbo, userMeetingSummary.get(userDbo.getId())));
        } catch (RestClientException restClientException) {
            log.warn("Failed to fetch meeting summaries", restClientException);
            return userDbos.map(userMapper::toDto);
        }
    }
}