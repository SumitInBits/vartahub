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
        name = "meeting_participants",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_meeting_participant",
                        columnNames = {"meeting_id", "user_id"}
                )
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class MeetingParticipantDbo extends BaseEntity {

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "meeting_id", nullable = false)
    private MeetingDbo meeting;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MeetingParticipantStatus meetingParticipantStatus;

    @OneToMany(
            mappedBy = "toParticipant",
            cascade = CascadeType.ALL
    )
    @Builder.Default
    private List<MeetingFeedbackDbo> feedbacks = new ArrayList<>();

    public void addFeedback(MeetingFeedbackDbo feedback) {
        feedbacks.add(feedback);
        feedback.setToParticipant(this);
    }
}