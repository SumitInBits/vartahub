package com.sumitinbits.vartahub.meeting.app.model;

import com.sumitinbits.vartahub.meeting.api.enums.MeetingType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MeetingAvailability {
    @Id
    private String id;
    private Instant availabilityStart;
    private Instant availabilityEnd;
    private MeetingType meetingType;
    private UUID userId;
    private UUID specialisationId;
}
