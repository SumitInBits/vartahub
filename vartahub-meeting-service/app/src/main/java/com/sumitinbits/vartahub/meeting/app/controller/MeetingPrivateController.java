package com.sumitinbits.vartahub.meeting.app.controller;

import com.sumitinbits.vartahub.meeting.api.dto.MeetingDto;
import com.sumitinbits.vartahub.meeting.app.service.MeetingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/private/meeting")
@Slf4j
@RequiredArgsConstructor
public class MeetingPrivateController {
    private final MeetingService meetingService;

    @GetMapping("/{meetingId}")
    public MeetingDto getMeeting(@PathVariable UUID meetingId) {
        return meetingService.getMeetingByIdUnsafe(meetingId);
    }
}
