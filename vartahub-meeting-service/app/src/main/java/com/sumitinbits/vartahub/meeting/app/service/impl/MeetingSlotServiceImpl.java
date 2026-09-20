package com.sumitinbits.vartahub.meeting.app.service.impl;

import com.sumitinbits.vartahub.meeting.api.enums.MeetingType;
import com.sumitinbits.vartahub.meeting.app.model.MeetingSlot;
import com.sumitinbits.vartahub.meeting.app.repository.MeetingSlotRepository;
import com.sumitinbits.vartahub.meeting.app.service.MeetingSlotService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MeetingSlotServiceImpl implements MeetingSlotService {
    private final MeetingSlotRepository meetingSlotRepository;

    @Override
    public String createMeetingSlot(MeetingSlot meetingSlot) {
        return meetingSlotRepository.save(meetingSlot).getId();
    }

    @Override
    public void deleteById(String id) {
        meetingSlotRepository.deleteById(id);
    }

    @Override
    public List<MeetingSlot> getMeetingSlot(UUID specialisationId, MeetingType meetingType) {
        return meetingSlotRepository.findByMeetingTypeAndSpecialisationId(meetingType, specialisationId);
    }

}
