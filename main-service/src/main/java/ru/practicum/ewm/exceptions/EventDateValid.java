package ru.practicum.ewm.exceptions;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target( { ElementType.FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = EventDateValidator.class)
@Documented
public @interface EventDateValid {
    String message() default "Start should be before than 2 hours from now";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
