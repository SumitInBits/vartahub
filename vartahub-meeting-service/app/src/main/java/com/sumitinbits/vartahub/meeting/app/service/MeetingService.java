package com.sumitinbits.vartahub.meeting.app.service;

import com.sumitinbits.vartahub.commons.dto.MeetingSummaryDto;
import com.sumitinbits.vartahub.meeting.api.dto.CreateMeetingRequest;
import com.sumitinbits.vartahub.meeting.api.dto.MeetingDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface MeetingService {
    void scheduleMeeting(CreateMeetingRequest createMeetingRequest);

    Page<MeetingDto> getMeetings(Pageable pageable);

    MeetingDto getMeetingByIdUnsafe(UUID meetingId);

    Map<UUID, MeetingSummaryDto> getUserMeetingsSummary(Set<UUID> userIds);
}
