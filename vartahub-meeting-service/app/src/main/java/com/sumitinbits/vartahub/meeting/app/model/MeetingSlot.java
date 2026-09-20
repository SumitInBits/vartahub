package com.sumitinbits.vartahub.meeting.app.model;

import com.sumitinbits.vartahub.meeting.api.enums.MeetingType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@RedisHash("meeting_availability_slot")
public class MeetingSlot implements Serializable {
    @Id
    private String id;
    private Instant startTime;
    private Instant endTime;
    @Indexed
    private MeetingType meetingType;
    private UUID meetingRequestId;
    @Indexed
    private UUID specialisationId;
    private UUID userId;
}