package ru.practicum.ewm.exceptions;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class EventDateValidator implements ConstraintValidator<EventDateValid, LocalDateTime> {
    @Override
    public void initialize(EventDateValid constraintAnnotation) {
    }

    @Override
    public boolean isValid(LocalDateTime eventDate, ConstraintValidatorContext constraintValidatorContext) {
        if (eventDate != null) {
            return eventDate.isAfter(LocalDateTime.now().plusHours(2));
        } else {
            return true;
        }
    }
}
