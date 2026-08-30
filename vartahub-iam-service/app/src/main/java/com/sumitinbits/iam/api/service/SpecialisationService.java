package com.sumitinbits.iam.api.service;

import com.sumitinbits.iam.api.dto.SpecialisationDto;
import com.sumitinbits.iam.api.dto.SpecialisationRequest;
import com.sumitinbits.iam.api.model.SpecialisationDbo;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface SpecialisationService {
    UUID createSpecialisation(SpecialisationRequest specialisationRequest);

    List<SpecialisationDto> getSpecialisations();

    List<SpecialisationDbo> getSpecialisationsDbosByIds(Set<UUID> specialisationIds);
}
