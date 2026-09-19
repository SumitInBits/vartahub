package com.sumitinbits.vartahub.meeting.app.model;

import com.sumitinbits.vartahub.meeting.api.enums.MeetingStatus;
import com.sumitinbits.vartahub.meeting.api.enums.MeetingType;
import jakarta.persistence.*;
import lombok.*;
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
                @Index(name = "meeting_target_user_id_idx", columnList = "target_user_id"),
                @Index(name = "meeting_scheduled_meeting_idx", columnList = "scheduled_meeting_id")
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
     * Can refer to instructorId when meeting type is INSTRUCTOR.
     */
    @Column(name = "target_user_id")
    private UUID targetUserId;

    /**
     * The actual scheduled meeting created from this meeting request.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scheduled_meeting_id")
    private ScheduledMeetingDbo scheduledMeeting;

    /**
     * Time slots proposed for this meeting request.
     *
     * Meeting owns the lifecycle of its proposals.
     */
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "time_slot_id", nullable = false)
    @Builder.Default
    private List<TimeSlotProposalDbo> timeSlotProposals = new ArrayList<>();

    public void addTimeSlotProposal(TimeSlotProposalDbo proposal) {
        timeSlotProposals.add(proposal);
    }

    public void removeTimeSlotProposal(TimeSlotProposalDbo proposal) {
        timeSlotProposals.remove(proposal);
    }
}