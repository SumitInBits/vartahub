package com.sumitinbits.vartahub.meeting.app.repository;

import com.sumitinbits.vartahub.meeting.app.model.MeetingDbo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MeetingRepository extends JpaRepository<MeetingDbo, UUID> {
    Page<MeetingDbo> findAllByUserId(UUID userId, Pageable pageable);

}
