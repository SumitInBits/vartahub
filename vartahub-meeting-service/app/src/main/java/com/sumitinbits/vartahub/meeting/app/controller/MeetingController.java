package com.sumitinbits.vartahub.meeting.app.controller;

import com.sumitinbits.vartahub.meeting.api.dto.ScheduleMeetingRequest;
import com.sumitinbits.vartahub.meeting.app.service.MeetingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/meeting")
@RequiredArgsConstructor
public class MeetingController {
    private final MeetingService meetingService;

    @PostMapping
    public void scheduleMeeting(@Valid @RequestBody ScheduleMeetingRequest scheduleMeetingRequest) {
        meetingService.scheduleMeeting(scheduleMeetingRequest);
    }


    @GetMapping
}
