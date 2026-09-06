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
                @Index(name = "scheduled_meetings_specialisation_idx", columnList = "specialisation_id"),
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class ScheduledMeetingDbo extends BaseEntity {
    @Column(nullable = false)
    private UUID specialisationId;

    private Instant startTime;

    private Instant endTime;

    @OneToMany(mappedBy = "scheduledMeeting", fetch = FetchType.LAZY)
    @Builder.Default
    private List<MeetingDbo> meetings = new ArrayList<>();

    @OneToMany(mappedBy = "scheduledMeeting", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Builder.Default
    private List<ScheduledMeetingParticipantDbo> participants = new ArrayList<>();

    public void addMeetingRequest(List<MeetingDbo> meetingDbos) {
        this.meetings.addAll(meetingDbos);
        meetingDbos.forEach(meetingDbo -> meetingDbo.setScheduledMeeting(this));
    }

    public void addParticipant(ScheduledMeetingParticipantDbo participant) {
        participants.add(participant);
        participant.setScheduledMeeting(this);
    }

    public void addParticipants(List<ScheduledMeetingParticipantDbo> participants) {
        this.participants.addAll(participants);
        participants.forEach(scheduledMeetingParticipantDbo ->
                scheduledMeetingParticipantDbo.setScheduledMeeting(this)
        );
    }
}