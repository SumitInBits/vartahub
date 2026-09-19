package com.sumitinbits.iam.vartahub.app.config;

import com.sumitinbits.iam.vartahub.app.client.MeetingServiceClient;
import com.sumitinbits.vartahub.commons.api.ApplicationProxy;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RestClientConfig {
    private final ApplicationProxy applicationProxy;

    @Value("${services.meeting-service}")
    private String meetingService;

    @Bean
    public MeetingServiceClient meetingServiceClient() {
        return applicationProxy.getApplicationClient(meetingService, MeetingServiceClient.class);
    }
}
