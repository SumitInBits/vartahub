package com.sumitinbits.vartahub.meeting.app.model;

import com.sumitinbits.vartahub.meeting.api.enums.ProposalStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@Entity
@Table(name = "time_slot_proposals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(
        callSuper = true,
        onlyExplicitlyIncluded = true
)
public class TimeSlotProposalDbo extends BaseEntity {
    @Column(name = "start_time", nullable = false)
    private Instant startTime;

    @Column(name = "end_time", nullable = false)
    private Instant endTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProposalStatus status;
}