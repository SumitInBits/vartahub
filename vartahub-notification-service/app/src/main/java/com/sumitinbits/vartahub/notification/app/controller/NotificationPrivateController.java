package com.sumitinbits.vartahub.notification.app.controller;

import com.sumitinbits.vartahub.notification.api.dto.NotificationRequest;
import com.sumitinbits.vartahub.notification.app.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/private/notification")
@RequiredArgsConstructor
@Slf4j
public class NotificationPrivateController {
    private final NotificationService notificationService;

    @PostMapping
    public void sendNotification(@RequestBody NotificationRequest notificationRequest) {
        notificationService.sendNotification(notificationRequest);
    }
}
