package com.sumitinbits.iam.vartahub.app.service;


import com.sumitinbits.iam.vartahub.app.model.SpecialisationDbo;
import com.sumitinbits.vartahub.iam.api.dto.SpecialisationDto;
import com.sumitinbits.vartahub.iam.api.dto.SpecialisationRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface SpecialisationService {
    UUID createSpecialisation(SpecialisationRequest specialisationRequest);

    Page<SpecialisationDto> getSpecialisations(Pageable pageable);

    List<SpecialisationDbo> getSpecialisationsDbosByIds(Set<UUID> specialisationIds);
}
