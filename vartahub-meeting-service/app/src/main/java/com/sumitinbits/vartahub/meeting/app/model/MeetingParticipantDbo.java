package com.sumitinbits.vartahub.meeting.app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(
        name = "meeting_participants",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "meeting_participant_unique",
                        columnNames = {"meeting_id", "participant_id"}
                )
        },
        indexes = {
                @Index(
                        name = "meeting_participant_participant_user_id_idx",
                        columnList = "participant_user_id"
                ),
                @Index(
                        name = "meeting_participant_meeting_idx",
                        columnList = "meeting_id"
                )
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class MeetingParticipantDbo extends BaseEntity {
    @Column(nullable = false)
    private UUID meetingId;

    @Column(nullable = false)
    private UUID participantUserId;

    @Column(length = 5000)
    private String comment;
}