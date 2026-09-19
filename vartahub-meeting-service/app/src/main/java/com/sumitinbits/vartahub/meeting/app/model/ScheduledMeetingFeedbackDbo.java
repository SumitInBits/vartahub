package com.sumitinbits.vartahub.meeting.app.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(
        name = "scheduled_meeting_feedbacks",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_scheduled_meeting_feedback",
                        columnNames = {
                                "from_participant_id",
                                "to_participant_id"
                        }
                )
        },
        indexes = {
                @Index(name = "feedback_from_participant_idx", columnList = "from_participant_id"),
                @Index(name = "feedback_to_participant_idx", columnList = "to_participant_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(
        callSuper = true,
        onlyExplicitlyIncluded = true
)
public class ScheduledMeetingFeedbackDbo extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "from_participant_id", nullable = false)
    private ScheduledMeetingParticipantDbo fromParticipant;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "to_participant_id", nullable = false)
    private ScheduledMeetingParticipantDbo toParticipant;

    private Integer rating;

    @Column(length = 2000)
    private String comment;
}