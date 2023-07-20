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
    public List<StatResponseDto> getStat(String start, String end, List<String> uris, Boolean unique) {
        List<StatView> hits;
        if (uris.isEmpty()) {
            if (unique) {
                hits = statRepository.findAllByTimestampBetweenUniqueIp(
                        LocalDateTime.parse(start, Constants.TIME_FORMATTER),
                        LocalDateTime.parse(end, Constants.TIME_FORMATTER));
            } else {
                hits = statRepository.findAllByTimestampBetween(
                        LocalDateTime.parse(start, Constants.TIME_FORMATTER),
                        LocalDateTime.parse(end, Constants.TIME_FORMATTER));
            }
        } else {
            if (unique) {
                hits = statRepository.findAllByTimestampBetweenAndUriInUniqueIp(
                        LocalDateTime.parse(start, Constants.TIME_FORMATTER),
                        LocalDateTime.parse(end, Constants.TIME_FORMATTER),
                        uris);
            } else {
                hits = statRepository.findAllByTimestampBetweenAndUriIn(
                        LocalDateTime.parse(start, Constants.TIME_FORMATTER),
                        LocalDateTime.parse(end, Constants.TIME_FORMATTER),
                        uris);
            }
        }
        return hits.stream().map(StatMapper::mapToStatResponse).collect(Collectors.toList());
    }
}
