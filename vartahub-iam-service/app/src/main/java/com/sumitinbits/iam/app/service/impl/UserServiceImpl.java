package com.sumitinbits.iam.app.service.impl;

import com.sumitinbits.iam.app.dto.UserDto;
import com.sumitinbits.iam.app.dto.UserRequest;
import com.sumitinbits.iam.app.dto.UserSpecialisationRequest;
import com.sumitinbits.iam.app.mapper.UserMapper;
import com.sumitinbits.iam.app.model.SpecialisationDbo;
import com.sumitinbits.iam.app.model.UserDbo;
import com.sumitinbits.iam.app.model.UserSpecialisationDbo;
import com.sumitinbits.iam.app.repository.UserRepository;
import com.sumitinbits.iam.app.service.IdentityProviderService;
import com.sumitinbits.iam.app.service.SpecialisationService;
import com.sumitinbits.iam.app.service.UserService;
import com.sumitinbits.iam.securitycore.util.AuthenticationUtil;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
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
    private final IdentityProviderService identityProviderService;

    @Override
    @Transactional
    public UUID createUser(UserRequest userRequest) {

        UUID identityProviderId = identityProviderService.createUser(userRequest);

        Set<UUID> specialisationIds = userRequest.specialisations()
                .stream()
                .map(UserSpecialisationRequest::id)
                .collect(Collectors.toSet());

        List<SpecialisationDbo> specialisationDbos = specialisationService.getSpecialisationsDbosByIds(specialisationIds);
        validateSpecialisations(specialisationIds, specialisationDbos);

        Map<UUID, SpecialisationDbo> specialisationMap = specialisationDbos.stream()
                        .collect(Collectors.toMap(SpecialisationDbo::getId, Function.identity()));

        UserDbo user = userMapper.toDbo(userRequest);

        List<UserSpecialisationDbo> userSpecialisations = userRequest.specialisations().stream()
                        .map(request -> {
                            SpecialisationDbo specialisation = specialisationMap.get(request.id());
                            return new UserSpecialisationDbo(user, specialisation, request.proficiencyLevel());
                        })
                        .toList();

        user.setSpecialisations(userSpecialisations);
        user.setIdentityProviderId(identityProviderId);

        return userRepository.save(user).getId();
    }

    @Override
    public UserDto getUserByIdUnsafe(UUID userId) {
        UserDbo user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found: " + userId));

        return userMapper.toDto(user);
    }

    @Override
    public UserDto getUser() {
        UUID identityProviderId = AuthenticationUtil.getAuthenticatedUser().identityProviderId();
        UserDbo user = userRepository.findByIdentityProviderId(identityProviderId)
                .orElseThrow(() -> new NotFoundException("User Not Found"));

        return userMapper.toDto(user);
    }

    private void validateSpecialisations(Set<UUID> requestedIds, List<SpecialisationDbo> foundSpecialisations) {
        if(requestedIds.size() != foundSpecialisations.size()) {
            throw new IllegalArgumentException("Some Specialisations not found");
        }

        foundSpecialisations.stream()
                .filter(specialisation -> Objects.nonNull(specialisation.getParent()))
                .findFirst()
                .ifPresent(specialisation -> {
                    throw new IllegalArgumentException("Specialisations should not be parent");
                });
    }
}