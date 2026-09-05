package com.sumitinbits.vartahub.notification.app.template;

import java.util.List;

public record MeetingScheduledDto(
        String topic,
        String scheduledAt,
        List<String> participantsFullName,
        String meetingURL
) {
}
