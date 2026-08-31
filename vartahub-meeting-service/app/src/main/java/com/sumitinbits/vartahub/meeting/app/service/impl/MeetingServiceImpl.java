package com.sumitinbits.vartahub.meeting.app.service.impl;

import com.sumitinbits.vartahub.meeting.app.service.MeetingMatchingService;
import com.sumitinbits.vartahub.meeting.app.service.MeetingService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MeetingServiceImpl implements MeetingService {
    private final MeetingMatchingService meetingMatchingService;

    @Override
    @Async
    public void scheduleMeeting() {

    }
}
