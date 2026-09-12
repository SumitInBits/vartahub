package com.sumitinbits.vartahub.meeting.app.controller;

import com.sumitinbits.vartahub.meeting.api.dto.CreateMeetingRequest;
import com.sumitinbits.vartahub.meeting.api.dto.MeetingDto;
import com.sumitinbits.vartahub.meeting.app.service.MeetingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/meeting")
@RequiredArgsConstructor
public class MeetingController {
    private final MeetingService meetingService;

    @PostMapping
    public void scheduleMeeting(@Valid @RequestBody CreateMeetingRequest createMeetingRequest) {
        meetingService.scheduleMeeting(createMeetingRequest);
    }

    public Page<MeetingDto> getMeetings(
            @PageableDefault(sort = "creationDate", direction = Sort.Direction.ASC)
            Pageable pageable
    ) {
        return meetingService.getMeetings(pageable);
    }
}
