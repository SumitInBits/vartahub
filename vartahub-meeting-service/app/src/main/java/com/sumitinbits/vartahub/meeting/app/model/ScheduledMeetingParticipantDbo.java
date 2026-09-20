package com.sumitinbits.vartahub.meeting.app.model;

import com.sumitinbits.vartahub.meeting.api.enums.MeetingParticipantStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Entity
@Table(
        name = "scheduled_meeting_participants",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_scheduled_meeting_participant",
                        columnNames = {
                                "scheduled_meeting_id",
                                "user_id"
                        }
                )
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
public class ScheduledMeetingParticipantDbo extends BaseEntity {
    @Column(nullable = false)
    private UUID userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private MeetingParticipantStatus status;
}