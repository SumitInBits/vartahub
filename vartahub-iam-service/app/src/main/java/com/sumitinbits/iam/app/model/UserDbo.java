package com.sumitinbits.iam.app.model;

import com.sumitinbits.iam.app.enums.Experience;
import com.sumitinbits.iam.app.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(
        name = "users", indexes = {
        @Index(name = "users_identity_provider_id_idx", columnList = "identity_provider_id")
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
    private UUID identityProviderId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Experience experience;

    private Integer experienceYears;

    private String organizationName;

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<UserSpecialisationDbo> specialisations = new ArrayList<>();
}
