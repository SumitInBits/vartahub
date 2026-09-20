package com.sumitinbits.vartahub.meeting.app.service;

import com.sumitinbits.vartahub.meeting.api.enums.MeetingType;
import com.sumitinbits.vartahub.meeting.app.model.MeetingSlot;

import java.util.List;
import java.util.UUID;

public interface MeetingSlotService {
    String createMeetingSlot(MeetingSlot meetingSlot);

    void deleteById(String id);

    List<MeetingSlot> getMeetingSlot(UUID specialisationId, MeetingType meetingType);
}
