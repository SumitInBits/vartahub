package com.sumitinbits.iam.vartahub.app.repository;

import com.sumitinbits.iam.vartahub.app.model.UserDbo;
import com.sumitinbits.vartahub.iam.api.enums.OnboardingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
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
}
