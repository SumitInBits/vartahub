package com.sumitinbits.iam.vartahub.app.service.impl;

import com.sumitinbits.iam.vartahub.app.mapper.UserMapper;
import com.sumitinbits.iam.vartahub.app.model.SpecialisationDbo;
import com.sumitinbits.iam.vartahub.app.model.UserDbo;
import com.sumitinbits.iam.vartahub.app.model.UserSpecialisationDbo;
import com.sumitinbits.iam.vartahub.app.repository.UserRepository;
import com.sumitinbits.iam.vartahub.app.service.IdentityService;
import com.sumitinbits.iam.vartahub.app.service.SpecialisationService;
import com.sumitinbits.iam.vartahub.app.service.UserService;
import com.sumitinbits.vartahub.commons.exception.OperationNotPermitted;
import com.sumitinbits.vartahub.commons.exception.ResourceNotFound;
import com.sumitinbits.vartahub.iam.api.dto.UserDto;
import com.sumitinbits.vartahub.iam.api.dto.UserRequest;
import com.sumitinbits.vartahub.iam.api.dto.UserSpecialisationRequest;
import com.sumitinbits.vartahub.iam.api.enums.Role;
import com.sumitinbits.vartahub.iam.securitycore.util.AuthenticationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final SpecialisationService specialisationService;
    private final IdentityService identityService;

    @Override
    @Transactional
    public UUID createUser(UserRequest userRequest) {
        List<Role> roles = getRoles(userRequest.role());

        Set<UUID> requestSpecialisationIds = userRequest.specialisations().stream()
                .map(UserSpecialisationRequest::specialisationId)
                .collect(Collectors.toSet());

        Map<UUID, SpecialisationDbo> specialisationDbos =
                specialisationService.getSpecialisationsDbosByIds(requestSpecialisationIds).stream()
                .collect(Collectors.toMap(SpecialisationDbo::getId, Function.identity()));

        if(requestSpecialisationIds.size() != specialisationDbos.size()) {
            throw new OperationNotPermitted("Some specialisations not found");
        }

        UserDbo userDbo = userMapper.toDbo(userRequest);

        List<UserSpecialisationDbo> userSpecialisations = userRequest.specialisations().stream()
                .map(userSpecialisation -> new UserSpecialisationDbo(
                        userDbo,
                        specialisationDbos.get(userSpecialisation.specialisationId()),
                        userSpecialisation.proficiency()
                )).toList();

        UUID identityId = identityService.createUser(userRequest, roles);
        userDbo.setUserSpecialisations(userSpecialisations);
        userDbo.setIdentityId(identityId);
        return userRepository.save(userDbo).getId();
    }

    @Override
    public UserDto getUserByIdUnsafe(UUID userId) {
        UserDbo userDbo = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFound("User not found " + userId));
        return userMapper.toDto(userDbo);
    }

    @Override
    public UserDto getUser() {
        UUID identityId = AuthenticationUtil.getAuthenticatedUser().identityId();
        UserDbo userDbo = userRepository.findByIdentityId(identityId)
                .orElseThrow(() -> new ResourceNotFound("User Not Found from Identity Provider " + identityId));

        return userMapper.toDto(userDbo);
    }

    private List<Role> getRoles(String requestedRole) {
        return switch (requestedRole.toUpperCase()) {
            case "USER" -> List.of(Role.USER);
            case "INSTRUCTOR" -> List.of(Role.INSTRUCTOR, Role.USER);
            default -> throw new UnsupportedOperationException("Unsupported role: " + requestedRole);
        };
    }
}