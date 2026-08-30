package com.sumitinbits.iam.app.service.impl;

import com.sumitinbits.iam.app.dto.SpecialisationDto;
import com.sumitinbits.iam.app.dto.SpecialisationRequest;
import com.sumitinbits.iam.app.mapper.SpecialisationMapper;
import com.sumitinbits.iam.app.model.SpecialisationDbo;
import com.sumitinbits.iam.app.repository.SpecialisationRepository;
import com.sumitinbits.iam.app.service.SpecialisationService;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SpecialisationServiceImpl implements SpecialisationService {

    private final SpecialisationRepository specialisationRepository;
    private final SpecialisationMapper specialisationMapper;

    @Override
    @Transactional
    public UUID createSpecialisation(SpecialisationRequest request) {
        String name = request.name().trim();
        String slug = generateSlug(name);

        if (specialisationRepository.existsBySlug(slug)) {
            throw new IllegalArgumentException("Specialisation already exists: " + name);
        }

        SpecialisationDbo parent = null;
        if (request.parentId() != null) {
            parent = specialisationRepository.findById(request.parentId())
                    .orElseThrow(() -> new NotFoundException(
                            "Parent specialisation not found: " + request.parentId()
                    ));
        }

        SpecialisationDbo specialisation = new SpecialisationDbo();
        specialisation.setName(name);
        specialisation.setSlug(slug);
        specialisation.setParent(parent);
        specialisation.setDepth(parent == null ? 0 : parent.getDepth() + 1);

        return specialisationRepository.save(specialisation).getId();
    }

    @Override
    public List<SpecialisationDto> getSpecialisations() {
        return createHierarchy(specialisationRepository.findAll());
    }

    @Override
    public SpecialisationDto getSpecialisationById(UUID specialisationId) {
        SpecialisationDbo specialisation = findById(specialisationId);
        return specialisationMapper.toDto(specialisation);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SpecialisationDto> getSpecialisationsByIds(Set<UUID> specialisationIds) {
        if (specialisationIds == null || specialisationIds.isEmpty()) {
            return List.of();
        }
        List<SpecialisationDbo> specialisations =
                specialisationRepository.findHierarchyByIds(specialisationIds);
        return createHierarchy(specialisations);
    }

    @Override
    public List<SpecialisationDbo> getSpecialisationsDbosByIds(Set<UUID> specialisationIds) {
        if (specialisationIds == null || specialisationIds.isEmpty()) {
            return List.of();
        }

        List<SpecialisationDbo> specialisations = specialisationRepository.findAllById(specialisationIds);
        validateAllFound(specialisationIds, specialisations);

        return specialisations;
    }

    private SpecialisationDbo findById(UUID id) {
        return specialisationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Specialisation not found: " + id));
    }

    private List<SpecialisationDbo> collectHierarchy(List<SpecialisationDbo> specialisations) {
        Map<UUID, SpecialisationDbo> hierarchy = new LinkedHashMap<>();

        for (SpecialisationDbo specialisation : specialisations) {
            SpecialisationDbo current = specialisation;
            while (current != null) {
                hierarchy.putIfAbsent(current.getId(), current);
                current = current.getParent();
            }
        }

        return new ArrayList<>(hierarchy.values());
    }

    private void validateAllFound(Set<UUID> requestedIds, List<SpecialisationDbo> found) {
        if (requestedIds.size() == found.size()) {
            return;
        }

        Set<UUID> foundIds = found.stream()
                .map(SpecialisationDbo::getId)
                .collect(Collectors.toSet());

        Set<UUID> missingIds = new HashSet<>(requestedIds);
        missingIds.removeAll(foundIds);

        throw new NotFoundException("Specialisations not found: " + missingIds);
    }

    private List<SpecialisationDto> createHierarchy(List<SpecialisationDbo> specialisations) {
        Map<UUID, SpecialisationDto> dtoMap = new LinkedHashMap<>(specialisations.size());

        for (SpecialisationDbo specialisation : specialisations) {
            dtoMap.put(specialisation.getId(), specialisationMapper.toDto(specialisation));
        }

        List<SpecialisationDto> roots = new ArrayList<>();

        for (SpecialisationDbo specialisation : specialisations) {
            SpecialisationDto current = dtoMap.get(specialisation.getId());
            SpecialisationDbo parent = specialisation.getParent();

            if (parent == null) {
                roots.add(current);
                continue;
            }

            SpecialisationDto parentDto = dtoMap.get(parent.getId());

            if (parentDto != null) {
                parentDto.children().add(current);
            }
        }

        return roots;
    }
    private String generateSlug(String name) {
        return name.toLowerCase(Locale.ROOT)
                .trim()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-|-$)", "");
    }
}