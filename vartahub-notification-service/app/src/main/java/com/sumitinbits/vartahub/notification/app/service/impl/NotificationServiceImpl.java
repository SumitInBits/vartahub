package com.sumitinbits.vartahub.notification.app.service.impl;

import com.sumitinbits.vartahub.notification.api.dto.NotificationRequest;
import com.sumitinbits.vartahub.notification.app.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    @Async
    @Override
    public void sendNotification(NotificationRequest notificationRequest) {

    }
}
