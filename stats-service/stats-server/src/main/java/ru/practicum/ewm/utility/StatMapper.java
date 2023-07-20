package ru.practicum.ewm.utility;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.dto.StatRequestDto;
import ru.practicum.ewm.dto.StatResponseDto;
import ru.practicum.ewm.model.Hit;
import ru.practicum.ewm.model.StatView;

@UtilityClass
public class StatMapper {
    public Hit mapToHit(StatRequestDto requestDto) {
        Hit hit = new Hit();
        hit.setApp(requestDto.getApp());
        hit.setUri(requestDto.getUri());
        hit.setIp(requestDto.getIp());
        hit.setTimestamp(requestDto.getTimestamp());
        return hit;
    }

    public StatResponseDto mapToStatResponse(StatView statView) {
        return new StatResponseDto(statView.getApp(), statView.getUri(), statView.getHits());
    }
}