package com.sumitinbits.vartahub.meeting.app.mapper;

import com.sumitinbits.vartahub.commons.dto.MeetingSummaryDto;
import com.sumitinbits.vartahub.meeting.api.dto.CreateMeetingRequest;
import com.sumitinbits.vartahub.meeting.api.dto.MeetingDto;
import com.sumitinbits.vartahub.meeting.app.model.MeetingDbo;
import com.sumitinbits.vartahub.meeting.app.model.projection.MeetingSummary;
import org.mapstruct.Mapper;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface MeetingMapper {
    MeetingDbo toDbo(CreateMeetingRequest createMeetingRequest, UUID userId);

    MeetingDto toDto(MeetingDbo meetingDbo);

    MeetingSummaryDto toUserMeetingSummaryDto(MeetingSummary meetingSummary);
}
