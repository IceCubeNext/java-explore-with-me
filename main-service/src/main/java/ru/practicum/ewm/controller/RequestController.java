package ru.practicum.ewm.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.dto.ParticipationRequestDto;
import ru.practicum.ewm.service.RequestService;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
public class RequestController {
    private final RequestService requestService;

    @GetMapping("/users/{userId}/requests")
    public List<ParticipationRequestDto> getUserRequests(@PathVariable Integer userId) {
        return requestService.getUserRequests(userId);
    }

    @GetMapping("/users/{userId}/events/{eventId}")
    public ParticipationRequestDto getParticipationRequestByUserId(@PathVariable Integer userId,
                                                                   @PathVariable Integer eventId,
                                                                   @RequestParam(defaultValue = "0") Integer from,
                                                                   @RequestParam(defaultValue = "10") Integer size) {
        return requestService.getParticipationRequestByUserId(userId, eventId, from, size);
    }

    @PostMapping("/users/{userId}/events/{eventId}/requests")
    public ParticipationRequestDto addUserRequest(@PathVariable Integer userId,
                                                  @PathVariable Integer eventId) {
        return requestService.addUserRequest(userId, eventId);
    }

    @PatchMapping("/users/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelUserRequest(@PathVariable Integer userId,
                                                     @PathVariable Integer requestId) {
        return requestService.cancelUserRequest(userId, requestId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResult updateParticipationRequestByUser(@PathVariable Integer userId,
                                                                           @PathVariable Integer eventId,
                                                                           @RequestBody EventRequestStatusUpdateRequest request) {
        return requestService.updateParticipationRequestByUser(userId, eventId, request);
    }
}
