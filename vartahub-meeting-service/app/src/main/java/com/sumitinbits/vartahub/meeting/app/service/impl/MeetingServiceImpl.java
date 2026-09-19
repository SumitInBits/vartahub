package com.sumitinbits.vartahub.meeting.app.service.impl;

import com.sumitinbits.vartahub.commons.dto.MeetingSummaryDto;
import com.sumitinbits.vartahub.commons.exception.ResourceNotFound;
import com.sumitinbits.vartahub.iam.api.dto.UserDto;
import com.sumitinbits.vartahub.iam.securitycore.util.AuthenticationUtil;
import com.sumitinbits.vartahub.meeting.api.dto.CreateMeetingRequest;
import com.sumitinbits.vartahub.meeting.api.dto.MatchedMeetingDto;
import com.sumitinbits.vartahub.meeting.api.dto.MeetingDto;
import com.sumitinbits.vartahub.meeting.api.enums.MeetingParticipantStatus;
import com.sumitinbits.vartahub.meeting.api.enums.MeetingType;
import com.sumitinbits.vartahub.meeting.api.enums.ProposalStatus;
import com.sumitinbits.vartahub.meeting.app.client.IamServiceClient;
import com.sumitinbits.vartahub.meeting.app.mapper.MeetingMapper;
import com.sumitinbits.vartahub.meeting.app.model.MeetingDbo;
import com.sumitinbits.vartahub.meeting.app.model.ScheduledMeetingDbo;
import com.sumitinbits.vartahub.meeting.app.model.ScheduledMeetingParticipantDbo;
import com.sumitinbits.vartahub.meeting.app.model.TimeSlotProposalDbo;
import com.sumitinbits.vartahub.meeting.app.model.projection.MeetingSummary;
import com.sumitinbits.vartahub.meeting.app.repository.MeetingRepository;
import com.sumitinbits.vartahub.meeting.app.repository.ScheduledMeetingParticipantRepository;
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
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MeetingServiceImpl implements MeetingService {
    private final MeetingMatchingService meetingMatchingService;
    private final MeetingRepository meetingRepository;
    private final ScheduledMeetingParticipantRepository scheduledMeetingParticipantRepository;
    private final ScheduledMeetingRepository scheduledMeetingRepository;
    private final IamServiceClient iamServiceClient;
    private final MeetingMapper meetingRequestMapper;

    @Override
    @Async
    @Transactional
    public void scheduleMeeting(CreateMeetingRequest createMeetingRequest) {
        UserDto userDto = getUser();
        TimeSlotProposalDbo timeSlotProposalDbo = new TimeSlotProposalDbo(
                createMeetingRequest.startTime(),
                createMeetingRequest.endTime(),
                ProposalStatus.PROPOSED
        );

        MeetingDbo meetingDbo = meetingRequestMapper.toDbo(createMeetingRequest, userDto.id());
        meetingDbo.addTimeSlotProposal(timeSlotProposalDbo);
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


    private void createMeeting(MatchedMeetingDto dto) {
        Set<UUID> meetingRequestIds = dto.meetingRequestIds();

        List<MeetingDbo> meetingDbos =
                meetingRepository.findAllById(meetingRequestIds);

        ScheduledMeetingDbo scheduledMeeting =
                ScheduledMeetingDbo.builder()
                        .startTime(dto.startTime())
                        .endTime(dto.endTime())
                        .specialisationId(dto.specialisationId())
                        .build();

        for (MeetingDbo meetingDbo : meetingDbos) {

            ScheduledMeetingParticipantDbo participant =
                    ScheduledMeetingParticipantDbo.builder()
                            .userId(meetingDbo.getUserId())
                            .meetingParticipantStatus(MeetingParticipantStatus.PENDING)
                            .build();

            scheduledMeeting.addParticipant(participant);
            meetingDbo.setScheduledMeeting(scheduledMeeting);
        }
        scheduledMeetingRepository.save(scheduledMeeting);
        meetingRepository.saveAll(meetingDbos);
    }

    private UserDto getUser() {
        UUID identityId = AuthenticationUtil.getAuthenticatedUser().keycloakId();
        return iamServiceClient.getUserByIdentityId(identityId);
    }

    @Override
    public Map<UUID, MeetingSummaryDto> getUserMeetingsSummary(Set<UUID> userIds) {
       List<MeetingSummary> meetingSummaryDbos =
               scheduledMeetingParticipantRepository.findScheduledMeetingParticipantSummary(userIds);

       return meetingSummaryDbos.stream()
                .collect(Collectors.toMap(
                                MeetingSummary::getUserId,
                                meetingRequestMapper::toUserMeetingSummaryDto
                        )
                );

    }
}
