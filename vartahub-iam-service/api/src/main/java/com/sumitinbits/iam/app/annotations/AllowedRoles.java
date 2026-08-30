package com.sumitinbits.iam.app.annotations;

import com.sumitinbits.iam.app.enums.Role;
import com.sumitinbits.iam.app.validator.AllowedRolesValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({
        ElementType.FIELD,
        ElementType.PARAMETER,
        ElementType.RECORD_COMPONENT
})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AllowedRolesValidator.class)
public @interface AllowedRoles {
    Role[] value();

    String message() default "Invalid role";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}