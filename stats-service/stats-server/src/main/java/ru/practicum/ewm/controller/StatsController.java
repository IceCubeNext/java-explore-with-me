package ru.practicum.ewm.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.StatRequestDto;
import ru.practicum.ewm.dto.StatResponseDto;
import ru.practicum.ewm.service.StatsService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@AllArgsConstructor
public class StatsController {
    private final StatsService statsService;

    @PostMapping("/hit")
    public void addHit(@RequestBody @Valid StatRequestDto requestDto) {
        statsService.addHit(requestDto);
    }

    @GetMapping("/stats")
    public List<StatResponseDto> getStats(@RequestParam @NotNull String start,
                                          @RequestParam @NotNull String end,
                                          @RequestParam(defaultValue = "") List<String> uris,
                                          @RequestParam(defaultValue = "false") Boolean unique) {
        return statsService.getStat(start, end, uris, unique);
    }
}
