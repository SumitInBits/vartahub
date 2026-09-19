package com.sumitinbits.vartahub.meeting.app.model.projection;

import java.util.UUID;

public interface ParticipantRating {
    UUID getParticipantId();
    Double getAverageRating();
}
