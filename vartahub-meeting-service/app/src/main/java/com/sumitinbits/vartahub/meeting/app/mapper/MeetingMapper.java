package com.sumitinbits.vartahub.meeting.app.mapper;

import com.sumitinbits.vartahub.commons.dto.MeetingSummaryDto;
import com.sumitinbits.vartahub.meeting.api.dto.*;
import com.sumitinbits.vartahub.meeting.app.model.MeetingDbo;
import com.sumitinbits.vartahub.meeting.app.model.ScheduledMeetingDbo;
import com.sumitinbits.vartahub.meeting.app.model.ScheduledMeetingParticipantDbo;
import com.sumitinbits.vartahub.meeting.app.model.TimeSlotProposalDbo;
import com.sumitinbits.vartahub.meeting.app.model.projection.MeetingSummary;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface MeetingMapper {
    MeetingDbo toDbo(MeetingRequest meetingRequest, UUID userId);

    MeetingDto toDto(MeetingDbo meetingDbo);

    TimeSlotProposalDto toDto(TimeSlotProposalDbo proposal);

    ScheduledMeetingParticipantDto toDto(ScheduledMeetingParticipantDbo scheduledMeetingParticipantDbo);

    ScheduledMeetingDto toDto(ScheduledMeetingDbo scheduledMeetingDbo);

    MeetingSummaryDto toUserMeetingSummaryDto(MeetingSummary meetingSummary);
}
