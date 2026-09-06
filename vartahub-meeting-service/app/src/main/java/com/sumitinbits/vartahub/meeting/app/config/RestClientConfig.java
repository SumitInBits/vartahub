package com.sumitinbits.vartahub.meeting.app.config;

import com.sumitinbits.vartahub.commons.api.ApplicationProxy;
import com.sumitinbits.vartahub.meeting.app.client.IamServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RestClientConfig {
    private final ApplicationProxy applicationProxy;

    @Value("${services.iam-service}")
    private String iamService;

    @Bean
    public IamServiceClient iamServiceClient() {
        return applicationProxy.getApplicationClient(iamService, IamServiceClient.class);
    }
}
