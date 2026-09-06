package com.sumitinbits.vartahub.meeting.app.repository;

import com.sumitinbits.vartahub.meeting.app.model.ScheduledMeetingDbo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ScheduledMeetingRepository extends JpaRepository<ScheduledMeetingDbo, UUID> {
}
