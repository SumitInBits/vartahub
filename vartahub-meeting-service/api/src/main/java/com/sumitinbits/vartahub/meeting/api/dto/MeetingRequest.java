package com.sumitinbits.vartahub.meeting.api.dto;

import com.sumitinbits.vartahub.meeting.api.enums.MeetingType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.UUID;

public record MeetingRequest(
        @NotNull Instant startTime,
        @NotNull Instant endTime,
        @NotNull UUID specialisationId,
        @Min(30) @Max(60)
        Integer duration,
        @NotNull MeetingType type
) {
}
