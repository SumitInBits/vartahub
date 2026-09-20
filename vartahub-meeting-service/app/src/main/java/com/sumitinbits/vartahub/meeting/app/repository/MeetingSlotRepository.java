package com.sumitinbits.vartahub.meeting.app.repository;


import com.sumitinbits.vartahub.meeting.api.enums.MeetingType;
import com.sumitinbits.vartahub.meeting.app.model.MeetingSlot;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MeetingSlotRepository extends CrudRepository<MeetingSlot, String> {
    List<MeetingSlot> findByMeetingTypeAndSpecialisationId(MeetingType meetingType, UUID specialisationId);
}
