package com.sumitinbits.iam.vartahub.app.repository;

import com.sumitinbits.iam.vartahub.app.model.UserDbo;
import com.sumitinbits.vartahub.iam.api.enums.OnboardingStatus;
import com.sumitinbits.vartahub.iam.api.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserDbo, UUID> {
    Optional<UserDbo> findByKeycloakId(UUID identityProviderId);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    @Query("""
            SELECT u.onboardingStatus
            FROM UserDbo u
            WHERE u.keycloakId = :keycloakId
            """)
    Optional<OnboardingStatus> findOnboardingStatusByKeycloakId(UUID keycloakId);

    @Query("""
        SELECT DISTINCT u
        FROM UserDbo u
        JOIN u.userSpecialisations us
        WHERE (:role IS NULL OR u.role = :role)
          AND (
                :specialisationIds IS NULL
                OR us.specialisation.id IN :specialisationIds
              )
          AND (
                :minExperienceYears IS NULL
                OR u.experienceYears >= :minExperienceYears
              )
        """)
    Page<UserDbo> findUsersByFilters(
            @Param("role") Role role,
            @Param("specialisationIds") Set<UUID> specialisationIds,
            @Param("minExperienceYears") Integer minExperienceYears,
            Pageable pageable
    );
}
