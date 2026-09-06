package com.sumitinbits.iam.vartahub.app.controller;

import com.sumitinbits.iam.vartahub.app.service.UserService;
import com.sumitinbits.vartahub.iam.api.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/private/iam")
@RequiredArgsConstructor
@Slf4j
public class IamPrivateController {
    private final UserService userService;

    @GetMapping("/users/{identityId}")
    UserDto getUserByIdentityId(@PathVariable UUID identityId) {
        return userService.getUserByIdentityUnsafe(identityId);
    }
}
