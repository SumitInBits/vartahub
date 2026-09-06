package com.sumitinbits.vartahub.meeting.app.model;

import com.sumitinbits.vartahub.meeting.api.enums.MeetingType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MeetingAvailabilitySlot {
    @Id
    private String id;
    private Instant startTime;
    private Instant endTime;
    private MeetingType meetingType;
    private UUID meetingRequestId;
    private UUID specialisationId;
}
