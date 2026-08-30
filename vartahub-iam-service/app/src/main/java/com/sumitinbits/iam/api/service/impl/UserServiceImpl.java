package com.sumitinbits.iam.api.service.impl;

import com.sumitinbits.iam.api.dto.UserDto;
import com.sumitinbits.iam.api.dto.UserRequest;
import com.sumitinbits.iam.api.dto.UserSpecialisationRequest;
import com.sumitinbits.iam.api.mapper.UserMapper;
import com.sumitinbits.iam.api.model.SpecialisationDbo;
import com.sumitinbits.iam.api.model.UserDbo;
import com.sumitinbits.iam.api.model.UserSpecialisationDbo;
import com.sumitinbits.iam.api.repository.UserRepository;
import com.sumitinbits.iam.api.service.IdentityProviderService;
import com.sumitinbits.iam.api.service.SpecialisationService;
import com.sumitinbits.iam.api.service.UserService;
import com.sumitinbits.iam.securitycore.util.AuthenticationUtil;
import jakarta.ws.rs.NotFoundException;
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
    private final IdentityProviderService identityProviderService;

    @Override
    @Transactional
    public UUID createUser(UserRequest userRequest) {
        Set<UUID> requestSpecialisationIds = userRequest.specialisations().stream()
                .map(UserSpecialisationRequest::specialisationId)
                .collect(Collectors.toSet());

        Map<UUID, SpecialisationDbo> specialisationDbos =
                specialisationService.getSpecialisationsDbosByIds(requestSpecialisationIds).stream()
                .collect(Collectors.toMap(SpecialisationDbo::getId, Function.identity()));

        if(requestSpecialisationIds.size() != specialisationDbos.size()) {
            throw new IllegalArgumentException("Some specialisations not found");
        }

        UserDbo userDbo = userMapper.toDbo(userRequest);

        List<UserSpecialisationDbo> userSpecialisations = userRequest.specialisations().stream()
                .map(userSpecialisation -> new UserSpecialisationDbo(
                        userDbo,
                        specialisationDbos.get(userSpecialisation.specialisationId()),
                        userSpecialisation.proficiencyLevel()
                )).toList();

        UUID identityProviderId = identityProviderService.createUser(userRequest);
        userDbo.setUserSpecialisations(userSpecialisations);
        userDbo.setIdentityProviderId(identityProviderId);

        return userRepository.save(userDbo).getId();
    }

    @Override
    public UserDto getUserByIdUnsafe(UUID userId) {
        UserDbo userDbo = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        return userMapper.toDto(userDbo);
    }

    @Override
    public UserDto getUser() {
        UUID identityProviderId = AuthenticationUtil.getAuthenticatedUser().identityProviderId();
        UserDbo userDbo = userRepository.findByIdentityProviderId(identityProviderId)
                .orElseThrow(() -> new NotFoundException("User Not Found"));

        return userMapper.toDto(userDbo);
    }
}