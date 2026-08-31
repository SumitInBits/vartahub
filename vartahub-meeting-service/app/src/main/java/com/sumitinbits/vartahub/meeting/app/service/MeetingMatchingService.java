package com.sumitinbits.vartahub.meeting.app.service;

import com.sumitinbits.vartahub.meeting.api.dto.ScheduleMeetingRequest;

import java.util.UUID;

public interface MeetingMatchingService {
    void submitMeetingMatch(ScheduleMeetingRequest scheduleMeetingRequest, UUID userId);
}
