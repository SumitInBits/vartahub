package com.sumitinbits.vartahub.meeting.app.repository;

import com.sumitinbits.vartahub.meeting.app.model.ScheduledMeetingParticipantDbo;
import com.sumitinbits.vartahub.meeting.app.model.projection.MeetingSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface ScheduledMeetingParticipantRepository extends JpaRepository<ScheduledMeetingParticipantDbo, UUID> {

    @Query("""
        SELECT
            p.userId AS userId,
            COUNT(p) AS totalMeetingsAttended,
            (
                SELECT AVG(f.rating)
                FROM ScheduledMeetingFeedbackDbo f
                WHERE f.toParticipant = p
            ) AS averageRating
        FROM ScheduledMeetingParticipantDbo p
        WHERE p.userId IN :userIds
        GROUP BY p.userId
        """)
    List<MeetingSummary> findScheduledMeetingParticipantSummary(@Param("userIds") Set<UUID> userIds);
}