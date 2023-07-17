package ru.practicum.ewm.exceptions;

import lombok.Generated;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;
import java.util.Map;

@Slf4j
@RestControllerAdvice
@Generated
public class ExceptionApiHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> illegalArgumentException(IllegalArgumentException exception) {
        log.error(exception.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap("error", exception.getMessage()));
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<Map<String, String>> throwableException(Throwable exception) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);
        log.error(sw.toString());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Collections.singletonMap("error", "Internal Server Error"));
    }
}
