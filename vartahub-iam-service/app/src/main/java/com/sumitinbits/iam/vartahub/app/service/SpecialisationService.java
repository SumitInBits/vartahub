package com.sumitinbits.iam.vartahub.app.service;


import com.sumitinbits.vartahub.iam.api.dto.SpecialisationDto;
import com.sumitinbits.vartahub.iam.api.dto.SpecialisationRequest;
import com.sumitinbits.iam.vartahub.app.model.SpecialisationDbo;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface SpecialisationService {
    UUID createSpecialisation(SpecialisationRequest specialisationRequest);

    List<SpecialisationDto> getSpecialisations();

    List<SpecialisationDbo> getSpecialisationsDbosByIds(Set<UUID> specialisationIds);
}
