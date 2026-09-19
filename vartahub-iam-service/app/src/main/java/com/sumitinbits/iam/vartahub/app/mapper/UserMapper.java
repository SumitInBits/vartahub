package com.sumitinbits.iam.vartahub.app.mapper;

import com.sumitinbits.vartahub.commons.dto.MeetingSummaryDto;
import com.sumitinbits.vartahub.iam.api.dto.UserDto;
import com.sumitinbits.iam.vartahub.app.model.UserDbo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(UserDbo userDbo);

    UserDto toDto(UserDbo userDbo, MeetingSummaryDto meetingSummaryDto);
}