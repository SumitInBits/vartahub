package com.sumitinbits.iam.api.service;

import com.sumitinbits.iam.api.dto.UserDto;
import com.sumitinbits.iam.api.dto.UserRequest;

import java.util.UUID;

public interface UserService {
    UUID createUser(UserRequest userRequest);

    UserDto getUserByIdUnsafe(UUID userId);

    UserDto getUser();
}
