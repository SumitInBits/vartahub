package com.sumitinbits.vartahub.meeting.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sumitinbits.vartahub.meeting.api.enums.MeetingStatus;
import com.sumitinbits.vartahub.meeting.api.enums.MeetingType;

import java.util.List;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record MeetingDto(
        UUID id,
        UUID userId,
        UUID specialisationId,
        MeetingType type,
        MeetingStatus status,
        UUID targetUserId,
        ScheduledMeetingDto scheduledMeeting,
        List<TimeSlotProposalDto> timeSlotProposals
) {
}
