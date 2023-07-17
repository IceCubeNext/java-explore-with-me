package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.StatRequestDto;
import ru.practicum.ewm.dto.StatResponseDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    void addHit(StatRequestDto requestDto);
    List<StatResponseDto> getStat(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
