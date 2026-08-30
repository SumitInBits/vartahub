package com.sumitinbits.iam.app.service;

import com.sumitinbits.iam.app.dto.UserDto;
import com.sumitinbits.iam.app.dto.UserRequest;

import java.util.UUID;

public interface UserService {
    UUID createUser(UserRequest userRequest);

    UserDto getUserByIdUnsafe(UUID userId);

    UserDto getUser();
}
