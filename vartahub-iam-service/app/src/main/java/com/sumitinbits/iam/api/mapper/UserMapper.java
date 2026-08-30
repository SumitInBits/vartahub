package com.sumitinbits.iam.api.mapper;

import com.sumitinbits.iam.api.dto.SpecialisationDto;
import com.sumitinbits.iam.api.dto.UserDto;
import com.sumitinbits.iam.api.dto.UserRequest;
import com.sumitinbits.iam.api.dto.UserSpecialisationDto;
import com.sumitinbits.iam.api.model.UserDbo;
import com.sumitinbits.iam.api.model.UserSpecialisationDbo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "identityProviderId", ignore = true)
    @Mapping(target = "profilePhotoKey", ignore = true)
    @Mapping(target = "userSpecialisations", ignore = true)
    UserDbo toDbo(UserRequest userRequest);

    UserDto toDto(UserDbo userDbo);
}