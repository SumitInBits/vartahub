package com.sumitinbits.iam.vartahub.app.service;

import com.sumitinbits.vartahub.iam.api.dto.CompleteCreateUserRequest;
import com.sumitinbits.vartahub.iam.api.dto.UserDto;

import java.util.UUID;

public interface UserService {
    UUID completeCreateUser(CompleteCreateUserRequest completeCreateUserRequest);

    UserDto getUserByIdUnsafe(UUID userId);

    UserDto getUserByIdentityUnsafe(UUID identityId);

    UserDto getUserOrCreate();
}
