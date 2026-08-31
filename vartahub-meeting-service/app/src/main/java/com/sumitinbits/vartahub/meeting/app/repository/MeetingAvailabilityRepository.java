package com.sumitinbits.vartahub.meeting.app.repository;

import com.sumitinbits.vartahub.meeting.api.enums.MeetingType;
import com.sumitinbits.vartahub.meeting.app.model.MeetingAvailability;

import java.util.List;
import java.util.UUID;

public interface MeetingAvailabilityRepository {
    MeetingAvailability save(MeetingAvailability availability);

    MeetingAvailability findById(UUID id);

    void deleteById(UUID id);

    List<MeetingAvailability> findCandidates(UUID specialisationId, MeetingType meetingType);
}
