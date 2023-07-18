package ru.practicum.ewm.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.StatClient;
import ru.practicum.ewm.dto.ResponseParametersDto;
import ru.practicum.ewm.dto.StatRequestDto;

import java.time.LocalDateTime;

@Slf4j
@RestController
@AllArgsConstructor
public class TestController {

    private final StatClient client = new StatClient("http://localhost:9090", new RestTemplateBuilder());

    @GetMapping("/events/{id}")
    public ResponseEntity<Object> getEvent(@PathVariable Integer id) {
        log.info("Get event with id={}", id);
        return client.addHit(new StatRequestDto("app1", "event/" + id, "127.0.0.1", LocalDateTime.now()));
    }

    @GetMapping("/stats")
    public ResponseEntity<Object> getStats(@RequestParam String start,
                                           @RequestParam String end,
                                           @RequestParam(defaultValue = "") String uris,
                                           @RequestParam(defaultValue = "false") Boolean unique) {
        log.info("Get stats from {} to {}, uris = {}, unique={}", start, end, uris, unique);
        return client.getStat(new ResponseParametersDto(start, end, uris, unique));
    }
}

