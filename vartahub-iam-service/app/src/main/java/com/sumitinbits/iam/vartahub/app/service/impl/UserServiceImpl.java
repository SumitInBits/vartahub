package com.sumitinbits.iam.vartahub.app.service.impl;

import com.sumitinbits.iam.vartahub.app.mapper.UserMapper;
import com.sumitinbits.iam.vartahub.app.model.SpecialisationDbo;
import com.sumitinbits.iam.vartahub.app.model.UserDbo;
import com.sumitinbits.iam.vartahub.app.model.UserSpecialisationDbo;
import com.sumitinbits.iam.vartahub.app.repository.UserRepository;
import com.sumitinbits.iam.vartahub.app.service.KeycloakService;
import com.sumitinbits.iam.vartahub.app.service.SpecialisationService;
import com.sumitinbits.iam.vartahub.app.service.UserService;
import com.sumitinbits.vartahub.commons.exception.OperationNotPermitted;
import com.sumitinbits.vartahub.commons.exception.ResourceNotFound;
import com.sumitinbits.vartahub.iam.api.dto.OnboardUserRequest;
import com.sumitinbits.vartahub.iam.api.dto.UserDto;
import com.sumitinbits.vartahub.iam.api.dto.UserSpecialisationRequest;
import com.sumitinbits.vartahub.iam.api.enums.OnboardingStatus;
import com.sumitinbits.vartahub.iam.api.enums.Role;
import com.sumitinbits.vartahub.iam.securitycore.util.AuthenticationUtil;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final SpecialisationService specialisationService;
    private final KeycloakService identityService;

    @Transactional
    public UUID onboardUser(OnboardUserRequest onboardUserRequest) {
        List<Role> roles = getRoles(onboardUserRequest.role());
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

        if(userDbo.isPresent() && userDbo.get().getOnboardingStatus() == OnboardingStatus.COMPLETED) {
            throw new OperationNotPermitted("User already completed onboarding");
        }

        UserRepresentation userRepresentation = identityService.getUser(keycloakId);
        UserDbo onboardingUser = UserDbo.builder()
                    .firstName(userRepresentation.getFirstName())
                    .lastName(userRepresentation.getLastName())
                    .email(userRepresentation.getEmail())
                    .username(userRepresentation.getUsername())
                    .keycloakId(UUID.fromString(userRepresentation.getId()))
                    .onboardingStatus(OnboardingStatus.PENDING)
                    .build();

        List<UserSpecialisationDbo> userSpecialisations = onboardUserRequest.specialisations().stream()
                .map(userSpecialisation -> new UserSpecialisationDbo(
                        onboardingUser,
                        specialisationDbos.get(userSpecialisation.specialisationId()),
                        userSpecialisation.proficiency()
                )).toList();

        identityService.assignRealmRole(keycloakId, roles);
        onboardingUser.setUserSpecialisations(userSpecialisations);
        onboardingUser.setOrganizationRole(onboardUserRequest.organizationRole());
        onboardingUser.setExperience(onboardUserRequest.experience());
        onboardingUser.setExperienceYears(onboardUserRequest.experienceYears());
        onboardingUser.setOrganizationName(onboardUserRequest.organizationName());
        onboardingUser.setKeycloakId(keycloakId);
        onboardingUser.setOnboardingStatus(OnboardingStatus.COMPLETED);
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
    public OnboardingStatus getUserOnboardingStatus() {
        UUID keycloakId = AuthenticationUtil.getAuthenticatedUser().keycloakId();
        return userRepository.findOnboardingStatusByKeycloakId(keycloakId).orElse(OnboardingStatus.PENDING);
    }

    private List<Role> getRoles(String requestedRole) {
        return switch (requestedRole.toUpperCase()) {
            case "USER" -> List.of(Role.USER);
            case "INSTRUCTOR" -> List.of(Role.INSTRUCTOR);
            default -> throw new UnsupportedOperationException("Unsupported role: " + requestedRole);
        };
    }
}