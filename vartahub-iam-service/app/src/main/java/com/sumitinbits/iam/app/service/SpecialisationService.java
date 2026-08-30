package com.sumitinbits.iam.app.service;

import com.sumitinbits.iam.app.dto.SpecialisationDto;
import com.sumitinbits.iam.app.dto.SpecialisationRequest;
import com.sumitinbits.iam.app.model.SpecialisationDbo;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface SpecialisationService {
    UUID createSpecialisation(SpecialisationRequest specialisationRequest);

    List<SpecialisationDto> getSpecialisations();

    SpecialisationDto getSpecialisationById(UUID specialisationId);

    List<SpecialisationDto> getSpecialisationsByIds(Set<UUID> specialisationIds);

    List<SpecialisationDbo> getSpecialisationsDbosByIds(Set<UUID> specialisationIds);
}
