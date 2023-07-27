package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.*;
import ru.practicum.ewm.model.Event;

import java.util.List;

public interface EventService {
    List<EventShortDto> getEvents(SearchEventParameters parameters, Integer from, Integer size);

    EventFullDto getEventById(Integer id, String ip, String uri);

    Event findById(Integer id);

    List<EventShortDto> getEventsByUserId(Integer userId, Integer from, Integer size);

    EventFullDto getEventByUserId(Integer userId, Integer eventId);

    List<EventFullDto> getEventsByAdmin(SearchEventParametersAdmin parameters, Integer from, Integer size);

    EventFullDto addEvent(Integer userId, NewEventDto eventDto);

    EventFullDto updateEvent(Integer userId, Integer eventId, UpdateEventUserRequest eventDto);

    EventFullDto updateEventByAdmin(Integer eventId, UpdateEventAdminRequest eventDto);
}
