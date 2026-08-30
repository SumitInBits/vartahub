package com.sumitinbits.iam.app.repository;

import com.sumitinbits.iam.app.model.SpecialisationDbo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface SpecialisationRepository extends JpaRepository<SpecialisationDbo, UUID> {
    boolean existsBySlug(String slug);

    @Query(value = """
        WITH RECURSIVE hierarchy AS (
            SELECT *
            FROM specialisations
            WHERE id IN (:ids)

            UNION

            SELECT parent.*
            FROM specialisations parent
            JOIN hierarchy child
                ON child.parent_id = parent.id
        )
        SELECT *
        FROM hierarchy
        """, nativeQuery = true)
    List<SpecialisationDbo> findHierarchyByIds(@Param("ids") Set<UUID> ids);
}