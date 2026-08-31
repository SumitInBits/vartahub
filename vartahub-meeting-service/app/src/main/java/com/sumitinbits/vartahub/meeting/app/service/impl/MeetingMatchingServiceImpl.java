package com.sumitinbits.vartahub.meeting.app.service.impl;

import com.sumitinbits.vartahub.meeting.api.dto.ScheduleMeetingRequest;
import com.sumitinbits.vartahub.meeting.api.enums.MeetingType;
import com.sumitinbits.vartahub.meeting.app.model.MeetingAvailability;
import com.sumitinbits.vartahub.meeting.app.repository.MeetingAvailabilityRepository;
import com.sumitinbits.vartahub.meeting.app.service.MeetingMatchingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MeetingMatchingServiceImpl implements MeetingMatchingService {
    private final MeetingAvailabilityRepository meetingAvailabilityRepository;

    @Override
    public void submitMeetingMatch(ScheduleMeetingRequest scheduleMeetingRequest, UUID userId) {
        List<MeetingAvailability> foundCandidates = findCandidates(
                userId,
                scheduleMeetingRequest.specialisationId(),
                scheduleMeetingRequest.meetingType(),
                scheduleMeetingRequest.availabilityStart(),
                scheduleMeetingRequest.availabilityEnd()
        );

        if(foundCandidates.isEmpty()) {
            return;
        }

        log.info("Found candidates: {}", foundCandidates);
    }

    public List<MeetingAvailability> findCandidates(UUID userId, UUID specialisationId, MeetingType meetingType, Instant start, Instant end) {
        return meetingAvailabilityRepository.findCandidates(specialisationId, meetingType)
                .stream()
                .filter(candidate -> !candidate.getUserId().equals(userId))
                .filter(candidate -> overlaps(candidate, start, end))
                .toList();
    }

    private boolean overlaps(MeetingAvailability candidate, Instant requestedStart, Instant requestedEnd) {
        return candidate.getAvailabilityStart().isBefore(requestedEnd)
                && candidate.getAvailabilityEnd().isAfter(requestedStart);
    }
}
