package com.sumitinbits.vartahub.meeting.app.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(
        name = "scheduled_meetings",
        indexes = {
                @Index(name = "scheduled_meeting_specialisation_idx", columnList = "specialisation_id"),
                @Index(name = "scheduled_meeting_start_time_idx", columnList = "start_time")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class ScheduledMeetingDbo extends BaseEntity {
    @Column(nullable = false)
    private UUID specialisationId;

    @Column(nullable = false)
    private Instant startTime;

    @Column(nullable = false)
    private Instant endTime;

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "scheduled_meeting_participant_id", nullable = false)
    private List<ScheduledMeetingParticipantDbo> participants = new ArrayList<>();

    public void addParticipant(ScheduledMeetingParticipantDbo participant
    ) {
        participants.add(participant);
    }

    public void removeParticipant(ScheduledMeetingParticipantDbo participant
    ) {
        participants.remove(participant);
    }
}