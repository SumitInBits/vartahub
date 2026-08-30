package com.sumitinbits.iam.api.mapper;

import com.sumitinbits.iam.api.dto.SpecialisationDto;
import com.sumitinbits.iam.api.dto.SpecialisationRequest;
import com.sumitinbits.iam.api.model.SpecialisationDbo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SpecialisationMapper {
    SpecialisationDto toDto(SpecialisationDbo dbo);
}
