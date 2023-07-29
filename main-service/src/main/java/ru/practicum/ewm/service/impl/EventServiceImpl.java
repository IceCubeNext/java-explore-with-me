package ru.practicum.ewm.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.practicum.ewm.StatClient;
import ru.practicum.ewm.dto.*;
import ru.practicum.ewm.exceptions.ConflictException;
import ru.practicum.ewm.exceptions.NotFoundException;
import ru.practicum.ewm.mapper.CategoryMapper;
import ru.practicum.ewm.mapper.EventMapper;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.enums.*;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.repository.RequestRepository;
import ru.practicum.ewm.service.CategoryService;
import ru.practicum.ewm.service.EventService;
import ru.practicum.ewm.service.UserService;
import ru.practicum.ewm.utility.Constants;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository repository;
    private final EventMapper mapper;
    private final UserService userService;
    private final CategoryService categoryService;
    private final RequestRepository requestRepository;
    private final CategoryMapper categoryMapper;
    private final StatClient statClient;

    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> getEvents(SearchEventParameters parameters, Integer from, Integer size) {
        LocalDateTime start = parameters.getRangeStart() == null ? null : LocalDateTime.parse(parameters.getRangeStart(), Constants.TIME_FORMATTER);
        LocalDateTime end = parameters.getRangeEnd() == null ? null : LocalDateTime.parse(parameters.getRangeEnd(), Constants.TIME_FORMATTER);

        if (start != null && end != null) {
            if (start.isAfter(end)) {
                throw new IllegalArgumentException("Start parameter of search should be before than end");
            }
        }

        List<Event> events = repository.findByUser(
                parameters.getText(),
                parameters.getCategories(),
                parameters.getPaid(),
                start,
                end,
                from,
                size);

        Map<Integer, Long> eventRequests = requestRepository.findAllByEventIdInAndStatus(
                        events.stream().map(Event::getId).collect(toList()), Status.CONFIRMED)
                .stream()
                .collect(Collectors.groupingBy(
                        r -> r.getEvent().getId(),
                        counting()
                ));

        if (parameters.getOnlyAvailable() != null && parameters.getOnlyAvailable()) {
            events = events.stream()
                    .filter(e -> (e.getParticipantLimit() >= eventRequests.get(e.getId())) || e.getParticipantLimit() == 0)
                    .collect(toList());
        }

        List<EventShortDto> eventShortDtoList = events.stream().map(mapper::toEventShortDto).collect(toList());
        for (EventShortDto event : eventShortDtoList) {
            if (eventRequests.containsKey(event.getId())) {
                event.setConfirmedRequests(eventRequests.get(event.getId()).intValue());
            } else {
                event.setConfirmedRequests(0);
            }
            event.setViews(event.getViews() + 1);
        }
        statClient.addHit(new StatRequestDto("ewm-main-service", "/events", parameters.getIp(), LocalDateTime.now()));
        if (Sort.EVENT_DATE.toString().equals(parameters.getSort())) {
            return eventShortDtoList.stream().sorted(Comparator.comparing(EventShortDto::getEventDate)).collect(toList());
        } else {
            return eventShortDtoList.stream().sorted(Comparator.comparing(EventShortDto::getViews)).collect(toList());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public EventFullDto getEventById(Integer id, String ip, String uri) {
        Event event = findById(id);
        if (event.getState() != Status.PUBLISHED) {
            throw new NotFoundException(String.format("Event with id=%d was not found", id));
        }
        event.setViews(event.getViews() + 1);
        statClient.addHit(new StatRequestDto("ewm-main-service", uri, ip, LocalDateTime.now()));
        EventFullDto eventFullDto = mapper.toEventFullDto(event);
        eventFullDto.setConfirmedRequests(requestRepository.countAllByEventIdAndStatus(id, Status.CONFIRMED));
        return eventFullDto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> getEventsByUserId(Integer userId, Integer from, Integer size) {
        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size);
        userService.findById(userId);
        return repository.findAllByInitiatorId(userId, page).map(mapper::toEventShortDto).getContent();
    }

    @Override
    @Transactional(readOnly = true)
    public EventFullDto getEventByUserId(Integer userId, Integer eventId) {
        userService.findById(userId);
        Event event = findById(eventId);
        if (!event.getInitiator().getId().equals(userId)) {
            throw new NotFoundException(String.format("Event with id=%d was not found", eventId));
        }
        event.setViews(event.getViews() + 1);
        EventFullDto eventFullDto = mapper.toEventFullDto(event);
        eventFullDto.setConfirmedRequests(requestRepository.countAllByEventIdAndStatus(eventId, Status.CONFIRMED));
        return eventFullDto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventFullDto> getEventsByAdmin(SearchEventParametersAdmin parameters, Integer from, Integer size) {
        LocalDateTime start = parameters.getRangeStart() == null ? null : LocalDateTime.parse(parameters.getRangeStart(), Constants.TIME_FORMATTER);
        LocalDateTime end = parameters.getRangeEnd() == null ? null : LocalDateTime.parse(parameters.getRangeEnd(), Constants.TIME_FORMATTER);

        if (start != null && end != null) {
            if (start.isAfter(end)) {
                throw new IllegalArgumentException("Start parameter of search should be before than end");
            }
        }

        List<Event> events = repository.findByAdmin(
                parameters.getUserIds(),
                parameters.getCategories(),
                parameters.getStates(),
                start,
                end,
                from,
                size);

        Map<Integer, Long> eventRequests = requestRepository.findAllByEventIdInAndStatus(events.stream().map(Event::getId).collect(toList()), Status.CONFIRMED)
                .stream()
                .collect(Collectors.groupingBy(
                        r -> r.getEvent().getId(),
                        counting()
                ));

        List<EventFullDto> eventFullDtoList = events.stream().map(mapper::toEventFullDto).collect(toList());
        for (EventFullDto event : eventFullDtoList) {
            if (eventRequests.containsKey(event.getId())) {
                event.setConfirmedRequests(eventRequests.get(event.getId()).intValue());
            } else {
                event.setConfirmedRequests(0);
            }
        }
        return eventFullDtoList;
    }

    @Override
    @Transactional
    public EventFullDto addEvent(Integer userId, NewEventDto eventDto) {
        Event event = mapper.toEvent(eventDto, userService.findById(userId), categoryService.findById(eventDto.getCategory()));
        if (event.getPaid() == null) {
            event.setPaid(false);
        }
        if (event.getParticipantLimit() == null) {
            event.setParticipantLimit(0);
        }
        if (event.getRequestModeration() == null) {
            event.setRequestModeration(true);
        }
        event.setState(Status.PENDING);
        event.setViews(0);
        event.setCreatedOn(LocalDateTime.now());
        return mapper.toEventFullDto(repository.save(event));
    }

    @Override
    @Transactional
    public EventFullDto updateEvent(Integer userId, Integer eventId, UpdateEventRequest eventDto) {
        userService.findById(userId);
        Event event = findById(eventId);

        if (!Objects.equals(event.getInitiator().getId(), userId)) {
            throw new ConflictException("Yor are not owner of this event");
        }

        if (Status.PUBLISHED.equals(event.getState())) {
            throw new ConflictException("Only pending or canceled events can be changed");
        }

        updateEventFields(event, eventDto);
        if (eventDto.getStateAction() != null) {
            if (StateAction.CANCEL_REVIEW.equals(eventDto.getStateAction())) {
                event.setState(Status.CANCELED);
            } else if (StateAction.SEND_TO_REVIEW.equals(eventDto.getStateAction())) {
                event.setState(Status.PENDING);
            } else {
                throw new IllegalArgumentException(String.format("Unknown event state: %s", event.getState()));
            }
        }
        return mapper.toEventFullDto(repository.save(event));
    }

    @Override
    @Transactional
    public EventFullDto updateEventByAdmin(Integer eventId, UpdateEventRequest eventDto) {
        Event event = findById(eventId);
        updateEventFields(event, eventDto);
        if (eventDto.getStateAction() != null) {
            if (StateAction.PUBLISH_EVENT.equals(eventDto.getStateAction())) {
                if (!event.getState().equals(Status.PENDING)) {
                    throw new ConflictException(String.format("Cannot publish the event because it's not in the right state: %s", event.getState()));
                } else {
                    event.setState(Status.PUBLISHED);
                    event.setPublishedOn(LocalDateTime.now());
                }
            } else if (StateAction.REJECT_EVENT.equals(eventDto.getStateAction())) {
                if (event.getState().equals(Status.PUBLISHED)) {
                    throw new ConflictException(String.format("Cannot publish the event because it's not in the right state: %s", event.getState()));
                } else {
                    event.setState(Status.CANCELED);
                }
            } else {
                throw new IllegalArgumentException(String.format("Unknown event state: %s", event.getState()));
            }
        }

        return mapper.toEventFullDto(repository.save(event));
    }

    @Override
    @Transactional
    public List<Event> findEventsByIdIn(List<Integer> ids) {
        return repository.findAllByIdIn(ids);
    }

    @Override
    @Transactional
    public Event findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Event with id=%d was not found", id)));
    }

    private void updateEventFields(Event event, UpdateEventRequest eventDto) {
        if (eventDto.getParticipantLimit() != null && !eventDto.getParticipantLimit().equals(event.getParticipantLimit())) {
            Integer requestCount = requestRepository.countAllByEventIdAndStatus(event.getId(), Status.CONFIRMED);
            if (eventDto.getParticipantLimit() < requestCount) {
                throw new ConflictException("New participant limit value smaller than exists confirmed requests");
            }
            event.setParticipantLimit(eventDto.getParticipantLimit());
        }
        if (eventDto.getCategory() != null && !eventDto.getCategory().equals(event.getCategory().getId())) {
            CategoryDto cat = categoryService.getCategoryById(eventDto.getCategory());
            event.setCategory(categoryMapper.toCategory(cat));
        }
        if (StringUtils.hasText(eventDto.getTitle()) && !eventDto.getTitle().equals(event.getTitle())) {
            event.setTitle(eventDto.getTitle());
        }
        if (StringUtils.hasText(eventDto.getDescription()) && !eventDto.getDescription().equals(event.getDescription())) {
            event.setDescription(eventDto.getDescription());
        }
        if (StringUtils.hasText(eventDto.getAnnotation()) && !eventDto.getAnnotation().equals(event.getAnnotation())) {
            event.setAnnotation(eventDto.getAnnotation());
        }
        if (eventDto.getEventDate() != null && !eventDto.getEventDate().equals(event.getEventDate())) {
            event.setEventDate(eventDto.getEventDate());
        }
        if (eventDto.getLocation() != null && !(Objects.equals(event.getLat(), eventDto.getLocation().getLat()) && Objects.equals(event.getLon(), eventDto.getLocation().getLon()))) {
            event.setLat(eventDto.getLocation().getLat());
            event.setLon(eventDto.getLocation().getLon());
        }
        if (eventDto.getPaid() != null && eventDto.getPaid() != event.getPaid()) {
            event.setPaid(eventDto.getPaid());
        }
        if (eventDto.getRequestModeration() != null && eventDto.getRequestModeration() != event.getRequestModeration()) {
            event.setRequestModeration(eventDto.getRequestModeration());
        }
    }
}
