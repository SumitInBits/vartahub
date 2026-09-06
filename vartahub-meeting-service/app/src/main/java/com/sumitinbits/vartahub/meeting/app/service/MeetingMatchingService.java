package com.sumitinbits.vartahub.meeting.app.service;

import com.sumitinbits.vartahub.meeting.api.dto.MatchedMeetingDto;
import com.sumitinbits.vartahub.meeting.app.model.MeetingDbo;

public interface MeetingMatchingService {
    MatchedMeetingDto submitMeetingMatch(MeetingDbo meetingDbo);
}
