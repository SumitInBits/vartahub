package com.sumitinbits.vartahub.meeting.app.controller;

import com.sumitinbits.vartahub.commons.dto.MeetingSummaryDto;
import com.sumitinbits.vartahub.meeting.api.dto.MeetingDto;
import com.sumitinbits.vartahub.meeting.app.service.MeetingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
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

    @PostMapping("/summary")
    public Map<UUID, MeetingSummaryDto> getUserMeetingSummary(@RequestBody Set<UUID> userIds) {
        return meetingService.getUserMeetingsSummary(userIds);
    }
}
