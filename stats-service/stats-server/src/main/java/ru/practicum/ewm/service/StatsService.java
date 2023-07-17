package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.StatRequestDto;
import ru.practicum.ewm.dto.StatResponseDto;

import java.util.List;

public interface StatsService {
    void addHit(StatRequestDto requestDto);

    List<StatResponseDto> getStat(String start, String end, List<String> uris, Boolean unique);
}
