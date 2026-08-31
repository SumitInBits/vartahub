package com.sumitinbits.iam.vartahub.app.repository;

import com.sumitinbits.iam.vartahub.app.model.SpecialisationDbo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SpecialisationRepository extends JpaRepository<SpecialisationDbo, UUID> {
    boolean existsBySlug(String slug);
}