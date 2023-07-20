package ru.practicum.ewm.validation;

import lombok.Generated;
import ru.practicum.ewm.dto.ResponseParametersDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Generated
public class StartEndValidator implements ConstraintValidator<StartBeforeEndDateValid, ResponseParametersDto> {
    @Override
    public void initialize(StartBeforeEndDateValid constraintAnnotation) {
    }

    @Override
    public boolean isValid(ResponseParametersDto parametersDto, ConstraintValidatorContext constraintValidatorContext) {
        LocalDateTime start = LocalDateTime.parse(parametersDto.getStart(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime end = LocalDateTime.parse(parametersDto.getEnd(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return start.isBefore(end);
    }
}
