package com.sumitinbits.vartahub.notification.api.dto;

import com.sumitinbits.vartahub.notification.api.enums.NotificationType;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public record NotificationRequest(
        List<UUID> userIds,
        NotificationType type,
        Map<String, Object> data
) {
}
