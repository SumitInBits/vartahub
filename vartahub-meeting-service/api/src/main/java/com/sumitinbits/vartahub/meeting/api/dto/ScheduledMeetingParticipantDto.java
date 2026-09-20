package com.sumitinbits.vartahub.meeting.api.dto;

import com.sumitinbits.vartahub.meeting.api.enums.MeetingParticipantStatus;

import java.util.UUID;

public record ScheduledMeetingParticipantDto(
   UUID userId,
   MeetingParticipantStatus status
) {
}
