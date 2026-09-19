package com.sumitinbits.vartahub.meeting.app.repository;


import com.sumitinbits.vartahub.meeting.app.model.ScheduledMeetingDbo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ScheduledMeetingRepository extends JpaRepository<ScheduledMeetingDbo, UUID> {
}
