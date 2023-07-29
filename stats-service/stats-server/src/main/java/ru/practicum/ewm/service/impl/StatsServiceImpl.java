package ru.practicum.ewm.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.StatRequestDto;
import ru.practicum.ewm.dto.StatResponseDto;
import ru.practicum.ewm.model.StatView;
import ru.practicum.ewm.repository.StatRepository;
import ru.practicum.ewm.service.StatsService;
import ru.practicum.ewm.utility.Constants;
import ru.practicum.ewm.utility.StatMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final StatRepository statRepository;

    @Override
    @Transactional
    public void addHit(StatRequestDto requestDto) {
        statRepository.save(StatMapper.mapToHit(requestDto));
    }

    @Override
    @Transactional(readOnly = true)
    public List<StatResponseDto> getStat(String startStat, String endSstat, List<String> uris, Boolean unique) {
        List<StatView> hits;

        LocalDateTime start = LocalDateTime.parse(startStat, Constants.TIME_FORMATTER);
        LocalDateTime end = LocalDateTime.parse(endSstat, Constants.TIME_FORMATTER);

        if (!start.isBefore(end)) {
            throw new IllegalArgumentException("start parameter should be before end");
        }

        if (uris.isEmpty()) {
            if (unique) {
                hits = statRepository.findAllByTimestampBetweenUniqueIp(start, end);
            } else {
                hits = statRepository.findAllByTimestampBetween(start, end);
            }
        } else {
            if (unique) {
                hits = statRepository.findAllByTimestampBetweenAndUriInUniqueIp(start, end, uris);
            } else {
                hits = statRepository.findAllByTimestampBetweenAndUriIn(start, end, uris);
            }
        }
        return hits.stream().map(StatMapper::mapToStatResponse).collect(Collectors.toList());
    }
}