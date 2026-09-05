package com.sumitinbits.iam.vartahub.app.controller;

import com.sumitinbits.vartahub.iam.api.dto.SpecialisationDto;
import com.sumitinbits.vartahub.iam.api.dto.SpecialisationRequest;
import com.sumitinbits.vartahub.iam.api.dto.UserDto;
import com.sumitinbits.vartahub.iam.api.dto.CompleteCreateUserRequest;
import com.sumitinbits.iam.vartahub.app.service.SpecialisationService;
import com.sumitinbits.iam.vartahub.app.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public List<SpecialisationDto> getSpecialisations() {
        log.info("API: get specialisations");
        return specialisationService.getSpecialisations();
    }

    @PostMapping("/users/complete")
    public UUID completeCreateUser(@Valid @RequestBody CompleteCreateUserRequest completeCreateUserRequest) {
        log.info("API: create user {}", completeCreateUserRequest);
        return userService.completeCreateUser(completeCreateUserRequest);
    }

    @GetMapping("/users")
    public UserDto getUser() {
        log.info("API: get user by context");
        return userService.getUserOrCreate();
    }
}
