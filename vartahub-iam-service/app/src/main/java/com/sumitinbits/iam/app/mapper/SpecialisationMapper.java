package com.sumitinbits.iam.app.mapper;

import com.sumitinbits.iam.app.dto.SpecialisationDto;
import com.sumitinbits.iam.app.model.SpecialisationDbo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SpecialisationMapper {
    @Mapping(target = "children", expression = "java(new java.util.ArrayList<>())")
    SpecialisationDto toDto(SpecialisationDbo dbo);
}
