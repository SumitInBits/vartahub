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
        name = "meeting_requests",
        indexes = {
                @Index(name = "meeting_requests_user_idx", columnList = "user_id"),
                @Index(name = "meeting_request_specialisation_idx", columnList = "specialisation_id"),
                @Index(name = "meeting_request_status_idx", columnList = "status"),
                @Index(name = "meeting_request_target_user_id_idx", columnList = "target_user_id")
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class MeetingRequestDbo extends BaseEntity {
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

    @ManyToOne
    @JoinColumn(name = "meeting_id")
    private MeetingDbo meeting;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "meeting_request_id", nullable = false)
    List<TimeSlotProposalDbo> timeSlotProposals = new ArrayList<>();
}