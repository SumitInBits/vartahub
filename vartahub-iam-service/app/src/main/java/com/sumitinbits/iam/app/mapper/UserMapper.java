package com.sumitinbits.iam.app.mapper;

import com.sumitinbits.iam.app.dto.UserDto;
import com.sumitinbits.iam.app.dto.UserRequest;
import com.sumitinbits.iam.app.dto.UserSpecialisationDto;
import com.sumitinbits.iam.app.model.UserDbo;
import com.sumitinbits.iam.app.model.UserSpecialisationDbo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "identityProviderId", ignore = true)
    @Mapping(target = "profilePhotoKey", ignore = true)
    @Mapping(target = "specialisations", ignore = true)
    UserDbo toDbo(UserRequest userRequest);

    UserSpecialisationDto toDto(UserSpecialisationDbo userSpecialisationDbo);

    UserDto toDto(UserDbo dbo);
}