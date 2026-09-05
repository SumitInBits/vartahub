package com.sumitinbits.vartahub.meeting.app.service;

import com.sumitinbits.vartahub.meeting.api.dto.ScheduleMeetingRequest;

public interface MeetingService {
    void scheduleMeeting(ScheduleMeetingRequest scheduleMeetingRequest);
}
