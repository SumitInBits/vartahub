package com.sumitinbits.iam.vartahub.app.mapper;

import com.sumitinbits.iam.vartahub.app.model.SpecialisationDbo;
import com.sumitinbits.vartahub.iam.api.dto.SpecialisationDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SpecialisationMapper {
    SpecialisationDto toDto(SpecialisationDbo dbo);
}
