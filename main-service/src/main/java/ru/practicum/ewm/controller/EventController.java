package ru.practicum.ewm.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.*;
import ru.practicum.ewm.model.enums.Status;
import ru.practicum.ewm.service.EventService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
public class EventController {
    private final EventService eventService;

    @GetMapping("/events")
    public List<EventShortDto> getEvents(@RequestParam(defaultValue = "") String text,
                                         @RequestParam(required = false) List<Integer> categories,
                                         @RequestParam(required = false) Boolean paid,
                                         @RequestParam(required = false) String rangeStart,
                                         @RequestParam(required = false) String rangeEnd,
                                         @RequestParam(required = false) Boolean onlyAvailable,
                                         @RequestParam(required = false) String sort,
                                         @RequestParam(defaultValue = "0") Integer from,
                                         @RequestParam(defaultValue = "10") Integer size) {
        SearchEventParameters parameters = new SearchEventParameters(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort);
        log.info("Get events text={}, categories={}, paid={}, start={}, end={}, available={}, sort={}, from={}, size={}"
                , text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
        return eventService.getEvents(parameters, from, size);
    }

    @GetMapping("/events/{id}")
    public EventFullDto getEventById(@PathVariable Integer id) {
        log.info("Get event with id={}", id);
        return eventService.getEventById(id);
    }

    @GetMapping("/users/{userId}/events")
    public List<EventShortDto> getEventsByUserId(@PathVariable Integer userId,
                                                 @RequestParam(defaultValue = "0") Integer from,
                                                 @RequestParam(defaultValue = "10") Integer size) {
        log.info("Get events with owner id={} from={} size={}", userId, from, size);
        return eventService.getEventsByUserId(userId, from, size);
    }

    @GetMapping("/users/{userId}/events/{eventId}")
    public EventFullDto getEventByUserId(@PathVariable Integer userId,
                                         @PathVariable Integer eventId) {
        log.info("Get event with id={} and user with id={}", eventId, userId);
        return eventService.getEventByUserId(userId, eventId);
    }

    @GetMapping("/admin/events")
    public List<EventFullDto> getEventsByAdmin(@RequestParam(required = false) List<Integer> users,
                                               @RequestParam(required = false) List<Status> states,
                                               @RequestParam(required = false) List<Integer> categories,
                                               @RequestParam(required = false) String rangeStart,
                                               @RequestParam(required = false) String rangeEnd,
                                               @RequestParam(defaultValue = "0") Integer from,
                                               @RequestParam(defaultValue = "10") Integer size) {
        SearchEventParametersAdmin parameters = new SearchEventParametersAdmin(users, states, categories, rangeStart, rangeEnd);
        log.info("Get events with ids={}, states={}, categories={}, start={}, end={}, from={}, size={} by admin"
                , users, states, categories, rangeStart, rangeEnd, from, size);
        return eventService.getEventsByAdmin(parameters, from, size);
    }

    @PostMapping("/users/{userId}/events")
    @ResponseStatus(code = HttpStatus.CREATED)
    public EventFullDto addEvent(@PathVariable Integer userId,
                                 @Valid @RequestBody NewEventDto eventDto) {
        log.info("Post event with owner id={}, event={}", userId, eventDto);
        return eventService.addEvent(userId, eventDto);
    }

    @PatchMapping("/users/{userId}/events/{eventId}")
    public EventFullDto updateEvent(@PathVariable Integer userId,
                                    @PathVariable Integer eventId,
                                    @Valid @RequestBody UpdateEventUserRequest eventDto) {
        log.info("Patch event with id={} and user id={}, event={}", eventId, userId, eventDto);
        return eventService.updateEvent(userId, eventId, eventDto);
    }

    @PatchMapping("/admin/events/{eventId}")
    public EventFullDto updateEventByAdmin(@PathVariable Integer eventId,
                                           @Valid @RequestBody UpdateEventAdminRequest request) {
        log.info("Patch event with id={} event request={}", eventId, request);
        return eventService.updateEventByAdmin(eventId, request);
    }
}
