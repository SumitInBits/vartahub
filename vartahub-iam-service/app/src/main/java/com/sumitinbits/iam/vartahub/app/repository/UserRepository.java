package com.sumitinbits.iam.vartahub.app.repository;

import com.sumitinbits.iam.vartahub.app.model.UserDbo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserDbo, UUID> {
    Optional<UserDbo> findByIdentityProviderId(UUID identityProviderId);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);
}
