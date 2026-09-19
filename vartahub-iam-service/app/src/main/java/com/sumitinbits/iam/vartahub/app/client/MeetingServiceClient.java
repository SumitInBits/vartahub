package com.sumitinbits.iam.vartahub.app.client;

import com.sumitinbits.vartahub.commons.dto.MeetingSummaryDto;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

@HttpExchange("/api/v1/private/meeting")
public interface MeetingServiceClient {
    @GetExchange("/summary")
    Map<UUID, MeetingSummaryDto> getUserMeetingSummary(@RequestBody Set<UUID> userIds);
}
