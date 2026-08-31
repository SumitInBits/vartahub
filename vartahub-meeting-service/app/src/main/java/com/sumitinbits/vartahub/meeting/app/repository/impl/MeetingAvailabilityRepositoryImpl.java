package com.sumitinbits.vartahub.meeting.app.repository.impl;

import com.sumitinbits.vartahub.meeting.api.enums.MeetingType;
import com.sumitinbits.vartahub.meeting.app.model.MeetingAvailability;
import com.sumitinbits.vartahub.meeting.app.repository.MeetingAvailabilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class MeetingAvailabilityRepositoryImpl implements MeetingAvailabilityRepository {

    private static final String KEY_PREFIX = "meeting-availability:";
    private static final String SPECIALISATION_INDEX = "meeting-availability:specialisation:";
    private static final String TYPE_INDEX = "meeting-availability:type:";

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public MeetingAvailability save(MeetingAvailability availability) {
        String key = KEY_PREFIX + availability.getId();
        redisTemplate.opsForValue().set(key, availability);
        addIndexes(availability);
        return availability;
    }

    @Override
    public MeetingAvailability findById(UUID id) {
        return (MeetingAvailability) redisTemplate.opsForValue()
                .get(KEY_PREFIX + id);
    }

    @Override
    public void deleteById(UUID id) {
        MeetingAvailability availability = findById(id);

        if (availability == null) {
            return;
        }

        removeIndexes(availability);
        redisTemplate.delete(KEY_PREFIX + id);
    }

    @Override
    public List<MeetingAvailability> findCandidates(UUID specialisationId, MeetingType meetingType) {
        Set<Object> ids = redisTemplate.opsForSet()
                .intersect(
                        specialisationKey(specialisationId),
                        typeKey(meetingType)
                );

        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }

        return ids.stream()
                .map(Object::toString)
                .map(UUID::fromString)
                .map(this::findById)
                .filter(Objects::nonNull)
                .toList();
    }

    private void addIndexes(MeetingAvailability availability) {
        redisTemplate.opsForSet().add(
                specialisationKey(availability.getSpecialisationId()),
                availability.getId()
        );

        redisTemplate.opsForSet().add(
                typeKey(availability.getMeetingType()),
                availability.getId()
        );
    }

    private void removeIndexes(MeetingAvailability availability) {
        redisTemplate.opsForSet().remove(
                specialisationKey(availability.getSpecialisationId()),
                availability.getId()
        );

        redisTemplate.opsForSet().remove(
                typeKey(availability.getMeetingType()),
                availability.getId()
        );
    }

    private String specialisationKey(UUID specialisationId) {
        return SPECIALISATION_INDEX + specialisationId;
    }

    private String typeKey(MeetingType meetingType) {
        return TYPE_INDEX + meetingType.name();
    }
}