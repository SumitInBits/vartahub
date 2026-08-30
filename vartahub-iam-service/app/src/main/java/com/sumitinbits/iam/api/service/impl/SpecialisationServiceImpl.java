package com.sumitinbits.iam.api.service.impl;

import com.sumitinbits.iam.api.dto.SpecialisationDto;
import com.sumitinbits.iam.api.dto.SpecialisationRequest;
import com.sumitinbits.iam.api.mapper.SpecialisationMapper;
import com.sumitinbits.iam.api.model.SpecialisationDbo;
import com.sumitinbits.iam.api.repository.SpecialisationRepository;
import com.sumitinbits.iam.api.service.SpecialisationService;
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
            throw new IllegalArgumentException("Specialisation already exists: " + name);
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