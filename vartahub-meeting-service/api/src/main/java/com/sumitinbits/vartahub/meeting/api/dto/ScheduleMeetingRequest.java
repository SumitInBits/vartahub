package com.sumitinbits.vartahub.meeting.api.dto;

import com.sumitinbits.vartahub.meeting.api.enums.MeetingType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.time.Instant;
import java.util.UUID;

public record ScheduleMeetingRequest(
        Instant availabilityStart,
        Instant availabilityEnd,
        UUID specialisationId,
        @Min(30) @Max(60)
        Integer duration,
        MeetingType meetingType
) {
}
