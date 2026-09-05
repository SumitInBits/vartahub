package com.sumitinbits.vartahub.notification.app.service;

import com.sumitinbits.vartahub.notification.api.dto.NotificationRequest;

public interface NotificationService {

    void sendNotification(NotificationRequest notificationRequest);
}
