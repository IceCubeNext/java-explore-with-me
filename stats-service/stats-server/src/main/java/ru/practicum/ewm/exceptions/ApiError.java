package ru.practicum.ewm.exceptions;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ApiError {
    private List<Violation> errors;
    private String message;
    private String reason;
    private HttpStatus status;
    private String timestamp;
}
