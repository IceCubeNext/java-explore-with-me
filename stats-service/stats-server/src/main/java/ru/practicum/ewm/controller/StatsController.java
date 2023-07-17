package ru.practicum.ewm.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.StatRequestDto;
import ru.practicum.ewm.service.StatsService;

import java.util.List;

@RestController
@AllArgsConstructor
public class StatsController {
    StatsService statsService;
    @PostMapping("/hit")
    public void addHit(@RequestBody StatRequestDto requestDto) {
        statsService.addHit(requestDto);
    }

    @GetMapping("/stats")
    public List<Object> getStats(@RequestParam String start,
                                 @RequestParam String end,
                                 @RequestParam List<String> uris,
                                 @RequestParam (defaultValue = "false") Boolean unique) {
        return statsService.getStat(start, end, uris, unique);
    }
}
