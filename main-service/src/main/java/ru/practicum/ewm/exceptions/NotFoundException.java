package ru.practicum.ewm.exceptions;

import lombok.Generated;

@Generated
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}