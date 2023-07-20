package ru.practicum.ewm.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.*;
import ru.practicum.ewm.model.enums.State;
import ru.practicum.ewm.service.EventService;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
public class EventController {
    private final EventService eventService;

    @GetMapping("/events")
    public List<EventShortDto> getEvents(@RequestParam String text,
                                         @RequestParam List<Integer> categories,
                                         @RequestParam Boolean paid,
                                         @RequestParam String rangeStart,
                                         @RequestParam String rangeEnd,
                                         @RequestParam Boolean onlyAvailable,
                                         @RequestParam String sort,
                                         @RequestParam(defaultValue = "0") Integer from,
                                         @RequestParam(defaultValue = "10") Integer size) {
        return eventService.getEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
    }

    @GetMapping("/events/{id}")
    public EventShortDto getEventById(@PathVariable Integer id) {
        return eventService.getEventById(id);
    }

    @GetMapping("/users/{userId}/events")
    public List<EventShortDto> getEventsByUserId(@PathVariable Integer userId,
                                                 @RequestParam(defaultValue = "0") Integer from,
                                                 @RequestParam(defaultValue = "10") Integer size) {
        return eventService.getEventsCurrentUser(userId, from, size);
    }

    @GetMapping("/users/{userId}/events/{eventId}")
    public EventFullDto getEventByUserId(@PathVariable Integer userId,
                                         @PathVariable Integer eventId,
                                         @RequestParam(defaultValue = "0") Integer from,
                                         @RequestParam(defaultValue = "10") Integer size) {
        return eventService.getEventByCurrentUser(userId, eventId, from, size);
    }

    @GetMapping("/admin/events")
    public List<EventFullDto> getEventsByAdmin(@RequestParam List<Integer> userIds,
                                               @RequestParam List<State> states,
                                               @RequestParam List<Integer> categories,
                                               @RequestParam String rangeStart,
                                               @RequestParam String rangeEnd,
                                               @RequestParam(defaultValue = "0") Integer from,
                                               @RequestParam(defaultValue = "10") Integer size) {
        return eventService.getEventsByAdmin(userIds, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PostMapping("/users/{userId}/events")
    public EventShortDto addEvent(@PathVariable Integer userId,
                                  @RequestBody NewEventDto eventDto) {
        return eventService.addEvent(userId, eventDto);
    }

    @PatchMapping("/users/{userId}/events/{eventId}")
    public EventShortDto updateEvent(@PathVariable Integer userId,
                                     @PathVariable Integer eventId,
                                     @RequestBody UpdateEventUserRequest eventDto) {
        return eventService.updateEvent(userId, eventId, eventDto);
    }

    @PatchMapping("/admin/events/{eventId}")
    public EventFullDto updateParticipationRequestByAdmin(@PathVariable Integer eventId,
                                                          @RequestBody UpdateEventAdminRequest request) {
        return eventService.updateParticipationRequestByAdmin(eventId, request);
    }
}
