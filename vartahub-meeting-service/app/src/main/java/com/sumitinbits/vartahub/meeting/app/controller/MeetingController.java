package com.sumitinbits.vartahub.meeting.app.controller;

import com.sumitinbits.vartahub.meeting.api.dto.ScheduleMeetingRequest;
import com.sumitinbits.vartahub.meeting.app.service.MeetingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/meeting")
@RequiredArgsConstructor
public class MeetingController {
    private final MeetingService meetingService;

    @PostMapping
    public void scheduleMeeting(@RequestBody ScheduleMeetingRequest scheduleMeetingRequest) {
        meetingService.scheduleMeeting();
    }
}
