package ru.practicum.ewm.mapper;

import org.mapstruct.Mapper;
import ru.practicum.ewm.dto.EventShortDto;
import ru.practicum.ewm.model.Event;

@Mapper
public interface EventMapper {
    EventShortDto toEventShortDto(Event event);
}
