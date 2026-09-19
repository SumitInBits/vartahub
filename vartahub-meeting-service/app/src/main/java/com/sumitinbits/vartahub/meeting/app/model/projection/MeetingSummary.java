package com.sumitinbits.vartahub.meeting.app.model.projection;

import java.util.UUID;

public interface MeetingSummary {
    UUID getUserId();
    Long getTotalMeetingsAttended();
    Double getAverageRating();
}
