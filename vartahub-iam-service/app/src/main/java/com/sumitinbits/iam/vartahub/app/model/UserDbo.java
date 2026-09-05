package com.sumitinbits.iam.vartahub.app.model;

import com.sumitinbits.vartahub.iam.api.enums.Experience;
import com.sumitinbits.vartahub.iam.api.enums.OnboardingStatus;
import com.sumitinbits.vartahub.iam.api.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(
        name = "users", indexes = {
        @Index(name = "users_identity_id_idx", columnList = "identity_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class UserDbo extends BaseEntity {
    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    private String profilePhotoKey;

    @Column(nullable = false)
    private UUID identityId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Experience experience;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OnboardingStatus onboardingStatus;

    private Integer experienceYears;

    private String organizationName;

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<UserSpecialisationDbo> userSpecialisations = new ArrayList<>();
}
