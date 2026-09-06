package com.sumitinbits.vartahub.meeting.app.repository;

import com.sumitinbits.vartahub.meeting.api.enums.MeetingType;
import com.sumitinbits.vartahub.meeting.app.model.MeetingAvailabilitySlot;

import java.util.List;
import java.util.UUID;

public interface MeetingAvailabilitySlotRepository {
    MeetingAvailabilitySlot save(MeetingAvailabilitySlot availability);

    MeetingAvailabilitySlot findById(UUID id);

    void deleteById(UUID id);

    List<MeetingAvailabilitySlot> findCandidates(UUID specialisationId, MeetingType meetingType);
}
