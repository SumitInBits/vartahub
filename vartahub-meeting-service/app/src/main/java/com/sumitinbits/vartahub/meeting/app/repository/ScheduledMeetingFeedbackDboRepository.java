package com.sumitinbits.vartahub.meeting.app.repository;

import com.sumitinbits.vartahub.meeting.app.model.ScheduledMeetingFeedbackDbo;
import com.sumitinbits.vartahub.meeting.app.model.projection.ParticipantRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ScheduledMeetingFeedbackDboRepository extends JpaRepository<ScheduledMeetingFeedbackDbo, UUID> {
    @Query("SELECT f.toParticipant.id as participantId, AVG(f.rating) as averageRating " +
            "FROM ScheduledMeetingFeedbackDbo f " +
            "WHERE f.toParticipant.id IN :participantIds " +
            "GROUP BY f.toParticipant.id")
    List<ParticipantRating> getParticipantRatings(@Param("participantIds") List<Long> participantIds);
}
