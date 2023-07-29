package ru.practicum.ewm.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.dto.ParticipationRequestDto;
import ru.practicum.ewm.service.RequestService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
public class RequestController {
    private final RequestService requestService;

    @GetMapping("/users/{userId}/requests")
    public List<ParticipationRequestDto> getUserRequests(@PathVariable Integer userId) {
        log.info("Get requests for user with id={}", userId);
        return requestService.getUserRequests(userId);
    }

    @GetMapping("/users/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> getParticipationRequestByUserId(@PathVariable Integer userId,
                                                                         @PathVariable Integer eventId,
                                                                         @RequestParam(defaultValue = "0") Integer from,
                                                                         @RequestParam(defaultValue = "10") Integer size) {
        log.info("Get participation requests for user with id={} on event with id={}", userId, eventId);
        return requestService.getParticipationRequestByUserId(userId, eventId, from, size);
    }

    @PostMapping("/users/{userId}/requests")
    @ResponseStatus(code = HttpStatus.CREATED)
    public ParticipationRequestDto addUserRequest(@PathVariable Integer userId,
                                                  @RequestParam Integer eventId) {
        log.info("Post request for user with id={} on event with id={}", userId, eventId);
        return requestService.addUserRequest(userId, eventId);
    }

    @PatchMapping("/users/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelUserRequest(@PathVariable Integer userId,
                                                     @PathVariable Integer requestId) {
        log.info("Cancel request with id={} from user with id={} ", requestId, userId);
        return requestService.cancelUserRequest(userId, requestId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResult updateParticipationRequestByUser(@PathVariable Integer userId,
                                                                           @PathVariable Integer eventId,
                                                                           @Valid @RequestBody EventRequestStatusUpdateRequest request) {
        log.info("Update event request status for event with id={} and user with id={} request={}", userId, eventId, request);
        return requestService.updateParticipationRequestByUser(userId, eventId, request);
    }
}
