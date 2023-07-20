package ru.practicum.ewm.exceptions;

import lombok.Generated;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Generated
@Getter
@RequiredArgsConstructor
public class ValidationErrorResponse {
    private final List<Violation> violations;

}