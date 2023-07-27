package ru.practicum.ewm.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.StatRequestDto;
import ru.practicum.ewm.dto.StatResponseDto;
import ru.practicum.ewm.service.StatsService;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
public class StatsController {
    private final StatsService statsService;

    @PostMapping("/hit")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void addHit(@RequestBody StatRequestDto requestDto) {
        log.info("Post hit from ip={}, app={}, uri={}, timestamp={}",
                requestDto.getIp(), requestDto.getApp(), requestDto.getUri(), requestDto.getTimestamp());
        statsService.addHit(requestDto);
    }

    @GetMapping("/stats")
    public List<StatResponseDto> getStats(@RequestParam String start,
                                          @RequestParam String end,
                                          @RequestParam(required = false) List<String> uris,
                                          @RequestParam(defaultValue = "false") Boolean unique) {
        log.info("Get stats from {} to {}, uris = {}, unique={}", start, end, uris, unique);
        return statsService.getStat(start, end, uris, unique);
    }
}
