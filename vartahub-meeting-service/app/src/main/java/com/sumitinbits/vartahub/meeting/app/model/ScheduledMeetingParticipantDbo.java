package com.sumitinbits.vartahub.meeting.app.model;

import com.sumitinbits.vartahub.meeting.api.enums.MeetingParticipantStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(
        name = "scheduled_meeting_participants",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_scheduled_meeting_participant",
                        columnNames = {"scheduled_meeting_id", "user_id"}
                )
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class ScheduledMeetingParticipantDbo extends BaseEntity {

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "scheduled_meeting_id", nullable = false)
    private ScheduledMeetingDbo scheduledMeeting;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MeetingParticipantStatus meetingParticipantStatus;

    @OneToMany(
            mappedBy = "toParticipant",
            cascade = CascadeType.ALL
    )
    @Builder.Default
    private List<ScheduledMeetingFeedbackDbo> feedbacks = new ArrayList<>();

    public void addFeedback(ScheduledMeetingFeedbackDbo feedback) {
        feedbacks.add(feedback);
        feedback.setToParticipant(this);
    }
}