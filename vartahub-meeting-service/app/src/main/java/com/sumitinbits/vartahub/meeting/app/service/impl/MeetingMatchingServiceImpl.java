package com.sumitinbits.vartahub.meeting.app.service.impl;

import com.sumitinbits.vartahub.meeting.api.dto.MatchedMeetingDto;
import com.sumitinbits.vartahub.meeting.api.enums.MeetingType;
import com.sumitinbits.vartahub.meeting.app.model.MeetingDbo;
import com.sumitinbits.vartahub.meeting.app.model.MeetingSlot;
import com.sumitinbits.vartahub.meeting.app.model.TimeSlotProposalDbo;
import com.sumitinbits.vartahub.meeting.app.service.MeetingMatchingService;
import com.sumitinbits.vartahub.meeting.app.service.MeetingSlotService;
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
    private final MeetingSlotService meetingSlotService;

    @Override
    public MatchedMeetingDto submitMeetingMatch(MeetingDbo meetingDbo) {
        TimeSlotProposalDbo requestedTimeSlot = meetingDbo.getTimeSlotProposals().getFirst();
        Instant requestedStart = requestedTimeSlot.getStartTime();
        Instant requestedEnd = requestedTimeSlot.getEndTime();

        List<MeetingSlot> candidates = findCandidates(
                meetingDbo.getId(),
                meetingDbo.getUserId(),
                meetingDbo.getSpecialisationId(),
                meetingDbo.getType(),
                requestedStart,
                requestedEnd
        );

        if (candidates.isEmpty()) {
            log.info(
                    "No meeting candidate found for meeting request {}",
                    meetingDbo.getId()
            );

            createMeetingSlot(meetingDbo, requestedStart, requestedEnd);

            return null;
        }

        MeetingSlot bestCandidate = candidates.stream()
                .max(Comparator.comparingLong(candidate -> {
                    Pair<Instant, Instant> meetingTime =
                            getOptimalMeetingTime(candidate, requestedStart, requestedEnd);
                    return Duration.between(meetingTime.getFirst(), meetingTime.getSecond()).toMillis();
                }))
                .orElseThrow();

        Pair<Instant, Instant> finalMeetingTime = getOptimalMeetingTime(
                        bestCandidate,
                        requestedStart,
                        requestedEnd
                );

        log.info("Meeting matched: meetingRequestId={} with meetingRequestId={} from {} to {}",
                meetingDbo.getId(),
                bestCandidate.getMeetingRequestId(),
                finalMeetingTime.getFirst(),
                finalMeetingTime.getSecond()
        );

        return createMatchedMeetingDto(meetingDbo, bestCandidate, finalMeetingTime);
    }

    private List<MeetingSlot> findCandidates(
            UUID meetingRequestId,
            UUID userId,
            UUID specialisationId,
            MeetingType meetingType,
            Instant requestedStart,
            Instant requestedEnd
    ) {
        return meetingSlotService.getMeetingSlot(specialisationId, meetingType)
                .stream()
                // Never match a meeting request with itself
                .filter(slot -> !slot.getMeetingRequestId().equals(meetingRequestId))
                .filter(slot -> !slot.getUserId().equals(userId))
                // Candidate must overlap requested time
                .filter(slot -> overlaps(slot, requestedStart, requestedEnd))
                .toList();
    }

    private boolean overlaps(MeetingSlot candidate, Instant requestedStart, Instant requestedEnd
    ) {
        return candidate.getStartTime().isBefore(requestedEnd) && candidate.getEndTime().isAfter(requestedStart);
    }

    private Pair<Instant, Instant> getOptimalMeetingTime(MeetingSlot candidate, Instant requestedStart, Instant requestedEnd) {
        Instant finalStart = candidate.getStartTime().isAfter(requestedStart) ? candidate.getStartTime() : requestedStart;
        Instant finalEnd = candidate.getEndTime().isBefore(requestedEnd) ? candidate.getEndTime() : requestedEnd;
        return Pair.of(finalStart, finalEnd);
    }

    private void createMeetingSlot(MeetingDbo meetingDbo, Instant startTime, Instant endTime) {
        MeetingSlot meetingSlot = MeetingSlot.builder()
                .meetingRequestId(meetingDbo.getId())
                .meetingType(meetingDbo.getType())
                .startTime(startTime)
                .endTime(endTime)
                .userId(meetingDbo.getUserId())
                .specialisationId(meetingDbo.getSpecialisationId())
                .build();

       String id = meetingSlotService.createMeetingSlot(meetingSlot);
       log.info("Meeting slot created {}", id);
    }

    private MatchedMeetingDto createMatchedMeetingDto(
            MeetingDbo originalRequest,
            MeetingSlot candidate,
            Pair<Instant, Instant> meetingTime
    ) {
        return new MatchedMeetingDto(
                meetingTime.getFirst(),
                meetingTime.getSecond(),
                originalRequest.getSpecialisationId(),
                Set.of(originalRequest.getId(), candidate.getMeetingRequestId())
        );
    }
}
