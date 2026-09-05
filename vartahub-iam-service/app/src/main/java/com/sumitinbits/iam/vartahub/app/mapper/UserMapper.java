package com.sumitinbits.iam.vartahub.app.mapper;

import com.sumitinbits.vartahub.iam.api.dto.UserDto;
import com.sumitinbits.vartahub.iam.api.dto.CompleteCreateUserRequest;
import com.sumitinbits.iam.vartahub.app.model.UserDbo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(UserDbo userDbo);
}