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
        name = "meetings",
        indexes = {
                @Index(name = "meetings_specialisation_idx", columnList = "specialisation_id"),
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class MeetingDbo extends BaseEntity {
    @Column(nullable = false)
    private UUID specialisationId;

    private Instant startTime;

    private Instant endTime;

    @OneToMany(mappedBy = "meeting", fetch = FetchType.LAZY)
    @Builder.Default
    private List<MeetingRequestDbo> meetingRequests = new ArrayList<>();

    @OneToMany(mappedBy = "meeting", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Builder.Default
    private List<MeetingParticipantDbo> participants = new ArrayList<>();

    public void addMeetingRequest(MeetingRequestDbo meetingRequest) {
        meetingRequests.add(meetingRequest);
        meetingRequest.setMeeting(this);
    }

    public void addParticipant(MeetingParticipantDbo participant) {
        participants.add(participant);
        participant.setMeeting(this);
    }
}