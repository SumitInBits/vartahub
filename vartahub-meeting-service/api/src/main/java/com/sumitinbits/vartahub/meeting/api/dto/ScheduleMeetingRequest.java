package com.sumitinbits.vartahub.meeting.api.dto;

import com.sumitinbits.vartahub.meeting.api.enums.MeetingType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.UUID;

public record ScheduleMeetingRequest(
        @NotNull Instant availabilityStart,
        @NotNull Instant availabilityEnd,
        @NotNull UUID specialisationId,
        @Min(30) @Max(60)
        Integer duration,
        @NotNull MeetingType meetingType
) {
}
