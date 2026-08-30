package com.sumitinbits.iam.app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(
        name = "user_specialisations",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_user_specialisation",
                        columnNames = {
                                "user_id",
                                "specialisation_id"
                        }
                )
        }
)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSpecialisationDbo extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "user_id",
            nullable = false
    )
    private UserDbo user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "specialisation_id",
            nullable = false
    )
    private SpecialisationDbo specialisation;
    /**
     * 1 = Beginner
     * 2 = Intermediate
     * 3 = Advanced
     * 4 = Expert
     * 5 = Master
     */
    private Integer proficiencyLevel;
}
