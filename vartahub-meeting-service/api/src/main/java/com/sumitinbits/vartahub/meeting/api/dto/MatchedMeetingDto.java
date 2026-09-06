package com.sumitinbits.vartahub.meeting.api.dto;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public record MatchedMeetingDto(
        Instant startTime,
        Instant endTime,
        UUID specialisationId,
        Set<UUID> meetingRequestIds
) {
}
