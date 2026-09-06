package com.sumitinbits.vartahub.meeting.app.service.impl;

import com.sumitinbits.vartahub.commons.exception.ResourceNotFound;
import com.sumitinbits.vartahub.iam.api.dto.UserDto;
import com.sumitinbits.vartahub.iam.securitycore.util.AuthenticationUtil;
import com.sumitinbits.vartahub.meeting.api.dto.CreateMeetingRequest;
import com.sumitinbits.vartahub.meeting.api.dto.MatchedMeetingDto;
import com.sumitinbits.vartahub.meeting.api.dto.MeetingDto;
import com.sumitinbits.vartahub.meeting.api.enums.MeetingParticipantStatus;
import com.sumitinbits.vartahub.meeting.api.enums.MeetingType;
import com.sumitinbits.vartahub.meeting.app.client.IamServiceClient;
import com.sumitinbits.vartahub.meeting.app.mapper.MeetingMapper;
import com.sumitinbits.vartahub.meeting.app.model.MeetingDbo;
import com.sumitinbits.vartahub.meeting.app.model.ScheduledMeetingDbo;
import com.sumitinbits.vartahub.meeting.app.model.ScheduledMeetingParticipantDbo;
import com.sumitinbits.vartahub.meeting.app.repository.MeetingRepository;
import com.sumitinbits.vartahub.meeting.app.repository.ScheduledMeetingRepository;
import com.sumitinbits.vartahub.meeting.app.service.MeetingMatchingService;
import com.sumitinbits.vartahub.meeting.app.service.MeetingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MeetingServiceImpl implements MeetingService {
    private final MeetingMatchingService meetingMatchingService;
    private final MeetingRepository meetingRepository;
    private final ScheduledMeetingRepository scheduledMeetingRepository;
    private final IamServiceClient iamServiceClient;
    private final MeetingMapper meetingRequestMapper;

    @Override
    @Async
    @Transactional
    public void scheduleMeeting(CreateMeetingRequest createMeetingRequest) {
        UserDto userDto = getUser();
        MeetingDbo meetingDbo = meetingRequestMapper.toDbo(createMeetingRequest, userDto.id());
        meetingRepository.save(meetingDbo);

        if(meetingDbo.getType() == MeetingType.USER) {
            MatchedMeetingDto matchedMeetingDto = meetingMatchingService.submitMeetingMatch(meetingDbo);
            if(matchedMeetingDto != null) {
                createMeeting(matchedMeetingDto);
            }
        }
    }

    @Override
    public Page<MeetingDto> getMeetings(Pageable pageable) {
        return meetingRepository.findAllByUserId(getUser().id(), pageable)
                .map(meetingRequestMapper::toDto);
    }

    @Override
    public MeetingDto getMeetingByIdUnsafe(UUID meetingId) {
        MeetingDbo meetingDbo = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new ResourceNotFound("Meeting not found " + meetingId));
        return meetingRequestMapper.toDto(meetingDbo);
    }


    private void createMeeting(MatchedMeetingDto matchedMeetingDto) {
        Set<UUID> meetingRequestIds = matchedMeetingDto.meetingRequestIds();
        List<MeetingDbo> meetingDbos = meetingRepository.findAllById(meetingRequestIds);

        List<ScheduledMeetingParticipantDbo> scheduledMeetingParticipantDbos = meetingDbos.stream()
                .map(meetingRequestDbo -> {
                 ScheduledMeetingParticipantDbo scheduledMeetingParticipantDbo = new ScheduledMeetingParticipantDbo();
                 scheduledMeetingParticipantDbo.setUserId(scheduledMeetingParticipantDbo.getId());
                 scheduledMeetingParticipantDbo.setMeetingParticipantStatus(MeetingParticipantStatus.PENDING);
                 return scheduledMeetingParticipantDbo;
                }).toList();

        ScheduledMeetingDbo scheduledMeetingDbo = ScheduledMeetingDbo.builder()
                .startTime(matchedMeetingDto.startTime())
                .endTime(matchedMeetingDto.endTime())
                .specialisationId(matchedMeetingDto.specialisationId())
                .build();

        scheduledMeetingDbo.setParticipants(scheduledMeetingParticipantDbos);
        scheduledMeetingDbo.setMeetings(meetingDbos);
        scheduledMeetingRepository.save(scheduledMeetingDbo);
    }

    private UserDto getUser() {
        UUID identityId = AuthenticationUtil.getAuthenticatedUser().identityId();
        return iamServiceClient.getUserByIdentityId(identityId);
    }
}
