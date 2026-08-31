package com.sumitinbits.iam.vartahub.app.model;

import com.sumitinbits.vartahub.iam.api.enums.Proficiency;
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

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(
            name = "specialisation_id",
            nullable = false
    )
    private SpecialisationDbo specialisation;

    @Enumerated(EnumType.STRING)
    private Proficiency proficiency;
}
