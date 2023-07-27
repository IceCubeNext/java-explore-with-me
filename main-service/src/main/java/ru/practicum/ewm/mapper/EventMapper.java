package ru.practicum.ewm.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.ewm.dto.EventFullDto;
import ru.practicum.ewm.dto.EventShortDto;
import ru.practicum.ewm.dto.NewEventDto;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.service.CategoryService;
import ru.practicum.ewm.service.UserService;

@Mapper(componentModel = "spring", uses = {CategoryMapper.class, UserMapper.class, CategoryService.class, UserService.class})
public interface EventMapper {
    EventShortDto toEventShortDto(Event event);

    @Mapping(target = "location", expression = "java(new ru.practicum.ewm.dto.Location(event.getLat(), event.getLon()))")
    EventFullDto toEventFullDto(Event event);

    @Mapping(source = "user", target = "initiator")
    @Mapping(source = "newEventDto.location.lat", target = "lat")
    @Mapping(source = "newEventDto.location.lon", target = "lon")
    @Mapping(target = "id", ignore = true)
    Event toEvent(NewEventDto newEventDto, User user);
}
