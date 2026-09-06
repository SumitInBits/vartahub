package com.sumitinbits.vartahub.meeting.app.service.impl;

import com.sumitinbits.vartahub.meeting.api.dto.MatchedMeetingDto;
import com.sumitinbits.vartahub.meeting.api.enums.MeetingType;
import com.sumitinbits.vartahub.meeting.app.model.MeetingAvailabilitySlot;
import com.sumitinbits.vartahub.meeting.app.model.MeetingDbo;
import com.sumitinbits.vartahub.meeting.app.model.TimeSlotProposalDbo;
import com.sumitinbits.vartahub.meeting.app.repository.MeetingAvailabilitySlotRepository;
import com.sumitinbits.vartahub.meeting.app.service.MeetingMatchingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MeetingMatchingServiceImpl implements MeetingMatchingService {
    private final MeetingAvailabilitySlotRepository meetingAvailabilitySlotRepository;

    // TODO: Consider refactoring this method.
    @Override
    public MatchedMeetingDto submitMeetingMatch(MeetingDbo meetingDbo) {

        TimeSlotProposalDbo requestedTimeSlot = meetingDbo.getTimeSlotProposals().getFirst();

        Instant requestedStart = requestedTimeSlot.getStartTime();
        Instant requestedEnd = requestedTimeSlot.getEndTime();

        List<MeetingAvailabilitySlot> candidates = findCandidates(
                meetingDbo.getUserId(),
                meetingDbo.getSpecialisationId(),
                meetingDbo.getType(),
                requestedStart,
                requestedEnd
        );

        if (candidates.isEmpty()) {
            log.info("No meeting candidate found for user {}", meetingDbo.getUserId());
        }

        /*
         * Pick the best candidate.
         *
         * Here we prefer the candidate having the largest
         * overlapping duration with the requested time.
         */
        MeetingAvailabilitySlot bestCandidate = candidates.stream()
                .max(Comparator.comparingLong(candidate -> {
                    Pair<Instant, Instant> meetingTime = getOptimalMeetingTime(candidate, requestedStart, requestedEnd);
                    return Duration.between(meetingTime.getFirst(), meetingTime.getSecond()).toMillis();
                }))
                .orElse(null);

        if(bestCandidate ==  null) {
            MeetingAvailabilitySlot availabilitySlot = MeetingAvailabilitySlot.builder()
                    .meetingRequestId(meetingDbo.getId())
                    .meetingType(meetingDbo.getType())
                    .startTime(meetingDbo.getTimeSlotProposals().getFirst().getStartTime())
                    .endTime(meetingDbo.getTimeSlotProposals().getFirst().getEndTime())
                    .specialisationId(meetingDbo.getSpecialisationId())
                    .build();
            meetingAvailabilitySlotRepository.save(availabilitySlot);
            return null;
        }

        Pair<Instant, Instant> finalMeetingTime = getOptimalMeetingTime(bestCandidate, requestedStart, requestedEnd);

        log.info("Meeting matched: user {} <-> user {} from {} to {}",
                meetingDbo.getUserId(), bestCandidate.getMeetingRequestId(), finalMeetingTime.getFirst(), finalMeetingTime.getSecond()
        );

        /*
         * IMPORTANT:
         * Exactly two users are returned:
         *
         * 1. Requesting meetingRequestId
         * 2. Matched meetingRequestId
         */
        return createMatchedMeetingDto(meetingDbo, bestCandidate, finalMeetingTime);
    }

    private List<MeetingAvailabilitySlot> findCandidates(UUID userId, UUID specialisationId, MeetingType meetingType, Instant start, Instant end) {

        return meetingAvailabilitySlotRepository.findCandidates(specialisationId, meetingType).stream()
                // Never match a user with themselves
                .filter(candidate -> !candidate.getMeetingRequestId().equals(userId))
                // Candidate must overlap requested time
                .filter(candidate -> overlaps(candidate, start, end))
                .toList();
    }

    private boolean overlaps(MeetingAvailabilitySlot candidate, Instant requestedStart, Instant requestedEnd) {
        return candidate.getStartTime().isBefore(requestedEnd) && candidate.getEndTime().isAfter(requestedStart);
    }

    private Pair<Instant, Instant> getOptimalMeetingTime(MeetingAvailabilitySlot candidate, Instant requestStart, Instant requestedEnd) {

        /*
         * Later start time
         */
        Instant finalStart = candidate.getStartTime().isAfter(requestStart) ? candidate.getStartTime() : requestStart;

        /*
         * Earlier end time
         */
        Instant finalEnd = candidate.getEndTime().isBefore(requestedEnd) ? candidate.getEndTime() : requestedEnd;

        return Pair.of(finalStart, finalEnd);
    }

    private MatchedMeetingDto createMatchedMeetingDto(MeetingDbo originalRequest, MeetingAvailabilitySlot candidate, Pair<Instant, Instant> meetingTime) {

        /*
         * Create the request representing the matched user.
         *
         * Adapt this part to your actual MeetingDbo
         * fields / builder.
         */

        return new MatchedMeetingDto(
                meetingTime.getFirst(),
                meetingTime.getSecond(),
                originalRequest.getSpecialisationId(),
                Set.of(originalRequest.getId(), candidate.getMeetingRequestId())
        );
    }
}
