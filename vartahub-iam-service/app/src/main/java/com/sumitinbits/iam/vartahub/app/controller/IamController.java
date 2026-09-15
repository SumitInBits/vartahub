package com.sumitinbits.iam.vartahub.app.controller;

import com.sumitinbits.iam.vartahub.app.service.SpecialisationService;
import com.sumitinbits.iam.vartahub.app.service.UserService;
import com.sumitinbits.vartahub.iam.api.dto.OnboardUserRequest;
import com.sumitinbits.vartahub.iam.api.dto.SpecialisationDto;
import com.sumitinbits.vartahub.iam.api.dto.SpecialisationRequest;
import com.sumitinbits.vartahub.iam.api.dto.UserDto;
import com.sumitinbits.vartahub.iam.api.enums.OnboardingStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/iam")
@RequiredArgsConstructor
@Slf4j
public class IamController {
    private final SpecialisationService specialisationService;
    private final UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/specialisations")
    public UUID createSpecialisation(@Valid @RequestBody SpecialisationRequest specialisationRequest) {
        log.info("API: create specialisation {}", specialisationRequest);
        return specialisationService.createSpecialisation(specialisationRequest);
    }

    @GetMapping("/specialisations")
    public Page<SpecialisationDto> getSpecialisations(
            @PageableDefault(sort = "creationDate", direction = Sort.Direction.ASC)
            Pageable pageable
    ) {
        log.info("API: get specialisations");
        return specialisationService.getSpecialisations(pageable);
    }

    @PostMapping("/users/onboard")
    public UUID onboardUser(@Valid @RequestBody OnboardUserRequest onboardUserRequest) {
        log.info("API: onboard user {}", onboardUserRequest);
        return userService.onboardUser(onboardUserRequest);
    }

    @GetMapping("/users/onboard/status")
    public OnboardingStatus onboardUser() {
        log.info("API: onboard status");
        return userService.getUserOnboardingStatus();
    }

    @GetMapping("/users")
    public UserDto getUser() {
        log.info("API: get user by context");
        return userService.getUser();
    }
}
