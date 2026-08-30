package com.sumitinbits.iam.app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Entity
@Table(
        name = "specialisations",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_specialisation_parent_name",
                        columnNames = {"parent_id", "name"}
                )
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SpecialisationDbo extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String slug;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private SpecialisationDbo parent;

    @Column(nullable = false)
    private Integer depth;
}
