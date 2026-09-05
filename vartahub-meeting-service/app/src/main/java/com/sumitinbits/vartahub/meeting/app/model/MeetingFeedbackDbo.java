package com.sumitinbits.vartahub.meeting.app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Entity
@Table(
        name = "meeting_feedbacks",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_meeting_feedback", columnNames = {"from_participant_id", "to_participant_id"})
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class MeetingFeedbackDbo extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "from_participant_id", nullable = false)
    private MeetingParticipantDbo fromParticipant;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "to_participant_id", nullable = false)
    private MeetingParticipantDbo toParticipant;

    @Column(length = 2000)
    private String feedback;
}