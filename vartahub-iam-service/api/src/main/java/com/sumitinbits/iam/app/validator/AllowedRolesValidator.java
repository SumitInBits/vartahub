package com.sumitinbits.iam.app.validator;

import com.sumitinbits.iam.app.annotations.AllowedRoles;
import com.sumitinbits.iam.app.enums.Role;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Set;

public class AllowedRolesValidator implements ConstraintValidator<AllowedRoles, Role> {
    private Set<Role> allowedRoles;

    @Override
    public void initialize(AllowedRoles annotation) {
        allowedRoles = Set.of(annotation.value());
    }

    @Override
    public boolean isValid(
            Role role,
            ConstraintValidatorContext context
    ) {
        return role == null || allowedRoles.contains(role);
    }
}