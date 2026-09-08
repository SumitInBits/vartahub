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
import com.sumitinbits.vartahub.iam.api.dto.CompleteCreateUserRequest;
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

    @Override
    @Transactional
    public UUID completeCreateUser(CompleteCreateUserRequest completeCreateUserRequest) {
        List<Role> roles = getRoles(completeCreateUserRequest.role());

        Set<UUID> requestSpecialisationIds = completeCreateUserRequest.specialisations().stream()
                .map(UserSpecialisationRequest::specialisationId)
                .collect(Collectors.toSet());

        Map<UUID, SpecialisationDbo> specialisationDbos =
                specialisationService.getSpecialisationsDbosByIds(requestSpecialisationIds).stream()
                .collect(Collectors.toMap(SpecialisationDbo::getId, Function.identity()));

        if(requestSpecialisationIds.size() != specialisationDbos.size()) {
            throw new OperationNotPermitted("Some specialisations not found");
        }

        UUID identityId = AuthenticationUtil.getAuthenticatedUser().keycloakId();
        UserDbo userDbo = userRepository.findByKeycloakId(identityId)
                .orElseThrow(() -> new ResourceNotFound("User not found"));

        List<UserSpecialisationDbo> userSpecialisations = completeCreateUserRequest.specialisations().stream()
                .map(userSpecialisation -> new UserSpecialisationDbo(
                        userDbo,
                        specialisationDbos.get(userSpecialisation.specialisationId()),
                        userSpecialisation.proficiency()
                )).toList();

        identityService.assignRealmRole(identityId, roles);
        userDbo.setUserSpecialisations(userSpecialisations);
        userDbo.setKeycloakId(identityId);
        return userRepository.save(userDbo).getId();
    }

    @Override
    public UserDto getUserByIdUnsafe(UUID userId) {
        UserDbo userDbo = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFound("User not found " + userId));
        return userMapper.toDto(userDbo);
    }

    @Override
    public UserDto getUserByIdentityUnsafe(UUID identityId) {
        UserDbo userDbo = userRepository.findByKeycloakId(identityId)
                .orElseThrow(() -> new ResourceNotFound("User not found " + identityId));
        return userMapper.toDto(userDbo);
    }

    @Override
    public UserDto getUserOrCreate() {
        UUID identityId = AuthenticationUtil.getAuthenticatedUser().keycloakId();
        Optional<UserDbo> userDbo = userRepository.findByKeycloakId(identityId);

        if(userDbo.isEmpty()) {
            UserRepresentation userRepresentation = identityService.getUser(identityId);
            UserDbo createUserDbo = UserDbo.builder()
                    .firstName(userRepresentation.getFirstName())
                    .lastName(userRepresentation.getLastName())
                    .email(userRepresentation.getEmail())
                    .username(userRepresentation.getUsername())
                    .keycloakId(UUID.fromString(userRepresentation.getId()))
                    .onboardingStatus(OnboardingStatus.PENDING)
                    .build();
            return userMapper.toDto(userRepository.save(createUserDbo));

        }
        return userMapper.toDto(userDbo.get());
    }

    private List<Role> getRoles(String requestedRole) {
        return switch (requestedRole.toUpperCase()) {
            case "USER" -> List.of(Role.USER);
            case "INSTRUCTOR" -> List.of(Role.INSTRUCTOR);
            default -> throw new UnsupportedOperationException("Unsupported role: " + requestedRole);
        };
    }
}