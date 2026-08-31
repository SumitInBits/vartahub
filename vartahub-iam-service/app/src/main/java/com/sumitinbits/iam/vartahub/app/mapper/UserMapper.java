package com.sumitinbits.iam.vartahub.app.mapper;

import com.sumitinbits.vartahub.iam.api.dto.UserDto;
import com.sumitinbits.vartahub.iam.api.dto.UserRequest;
import com.sumitinbits.iam.vartahub.app.model.UserDbo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "identityId", ignore = true)
    @Mapping(target = "profilePhotoKey", ignore = true)
    @Mapping(target = "userSpecialisations", ignore = true)
    UserDbo toDbo(UserRequest userRequest);

    UserDto toDto(UserDbo userDbo);
}