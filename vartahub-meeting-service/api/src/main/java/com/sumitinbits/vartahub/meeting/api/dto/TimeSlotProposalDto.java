package com.sumitinbits.vartahub.meeting.api.dto;

import com.sumitinbits.vartahub.meeting.api.enums.ProposalStatus;

import java.time.Instant;

public record TimeSlotProposalDto(
    Instant startTime,
    Instant endTime,
    ProposalStatus status
) {
}
