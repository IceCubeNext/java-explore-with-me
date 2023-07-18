package ru.practicum.ewm.exceptions;

import lombok.Generated;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Generated
@Getter
@RequiredArgsConstructor
public class Violation {
    private final String fieldName;
    private final String message;
}
