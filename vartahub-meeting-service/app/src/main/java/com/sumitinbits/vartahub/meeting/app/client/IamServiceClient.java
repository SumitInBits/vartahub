package com.sumitinbits.vartahub.meeting.app.client;

import com.sumitinbits.vartahub.iam.api.dto.UserDto;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.UUID;

@HttpExchange("/api/v1/private/iam")
public interface IamServiceClient {
    @GetExchange("/users/{identityId}")
    UserDto getUserByIdentityId(@PathVariable UUID identityId);
}
