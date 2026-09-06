package com.sumitinbits.vartahub.meeting.app.model;

import com.sumitinbits.vartahub.meeting.api.enums.MeetingStatus;
import com.sumitinbits.vartahub.meeting.api.enums.MeetingType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(
        name = "meetings",
        indexes = {
                @Index(name = "meeting_user_idx", columnList = "user_id"),
                @Index(name = "meeting_specialisation_idx", columnList = "specialisation_id"),
                @Index(name = "meeting_status_idx", columnList = "status"),
                @Index(name = "meeting_target_user_id_idx", columnList = "target_user_id")
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class MeetingDbo extends BaseEntity {
    @Column(nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private UUID specialisationId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MeetingType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MeetingStatus status;

    /**
     * targetUserId; can refer to instructorId if meetingType selected as instructor
     */
    private UUID targetUserId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "scheduled_meeting_id")
    private ScheduledMeetingDbo scheduledMeeting;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "meeting_time_slot_id", nullable = false)
    List<TimeSlotProposalDbo> timeSlotProposals = new ArrayList<>();
}