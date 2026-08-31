package com.sumitinbits.iam.vartahub.app.service;

import com.sumitinbits.vartahub.iam.api.dto.UserDto;
import com.sumitinbits.vartahub.iam.api.dto.UserRequest;

import java.util.UUID;

public interface UserService {
    UUID createUser(UserRequest userRequest);

    UserDto getUserByIdUnsafe(UUID userId);

    UserDto getUser();
}
