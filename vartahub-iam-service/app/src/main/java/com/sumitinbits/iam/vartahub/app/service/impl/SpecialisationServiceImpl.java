package com.sumitinbits.iam.vartahub.app.service.impl;

import com.sumitinbits.iam.vartahub.app.mapper.SpecialisationMapper;
import com.sumitinbits.iam.vartahub.app.model.SpecialisationDbo;
import com.sumitinbits.iam.vartahub.app.repository.SpecialisationRepository;
import com.sumitinbits.iam.vartahub.app.service.SpecialisationService;
import com.sumitinbits.vartahub.commons.exception.OperationNotPermitted;
import com.sumitinbits.vartahub.iam.api.dto.SpecialisationDto;
import com.sumitinbits.vartahub.iam.api.dto.SpecialisationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SpecialisationServiceImpl implements SpecialisationService {
    private final SpecialisationRepository specialisationRepository;
    private final SpecialisationMapper specialisationMapper;

    @Override
    @Transactional
    public UUID createSpecialisation(SpecialisationRequest specialisationRequest) {
        String name = specialisationRequest.name().trim();
        String slug = generateSlug(name);

        if (specialisationRepository.existsBySlug(slug)) {
            throw new OperationNotPermitted("Specialisation already exists: " + name);
        }

        SpecialisationDbo specialisationDbo = new SpecialisationDbo(specialisationRequest.name(), slug);
        return specialisationRepository.save(specialisationDbo).getId();
    }

    @Override
    public List<SpecialisationDto> getSpecialisations() {
        return specialisationRepository.findAll().stream()
                .map(specialisationMapper::toDto)
                .toList();
    }

    @Override
    public List<SpecialisationDbo> getSpecialisationsDbosByIds(Set<UUID> specialisationIds) {
        return specialisationRepository.findAllById(specialisationIds);
    }

    private String generateSlug(String name) {
        return name.toLowerCase(Locale.ROOT)
                .trim()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-|-$)", "");
    }
}