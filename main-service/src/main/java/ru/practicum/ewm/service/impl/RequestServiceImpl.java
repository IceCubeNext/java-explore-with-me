package ru.practicum.ewm.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.exceptions.AlreadyExistsException;
import ru.practicum.ewm.exceptions.ConflictException;
import ru.practicum.ewm.exceptions.NotFoundException;
import ru.practicum.ewm.mapper.RequestMapper;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.Request;
import ru.practicum.ewm.model.enums.Status;
import ru.practicum.ewm.repository.RequestRepository;
import ru.practicum.ewm.service.EventService;
import ru.practicum.ewm.service.RequestService;
import ru.practicum.ewm.service.UserService;
import ru.practicum.ewm.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.dto.ParticipationRequestDto;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final UserService userService;
    private final EventService eventService;
    private final RequestRepository repository;
    private final RequestMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> getUserRequests(Integer userId) {
        userService.findById(userId);
        return repository.findAllByRequesterId(userId).stream().map(mapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> getParticipationRequestByUserId(Integer userId, Integer eventId, Integer from, Integer size) {
        userService.findById(userId);
        eventService.findById(eventId);
        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size);
        return repository.findAllByEventIdAndEventInitiatorId(eventId, userId, page)
                .map(mapper::toParticipationRequestDto)
                .getContent();
    }

    @Override
    @Transactional
    public ParticipationRequestDto addUserRequest(Integer userId, Integer eventId) {
        userService.findById(userId);
        Event event = eventService.findById(eventId);
        if (Objects.equals(event.getInitiator().getId(), userId)) {
            throw new ConflictException("You are the owner of the required event");
        }
        if (!Status.PUBLISHED.equals(event.getState())) {
            throw new ConflictException("The required event not available");
        }
        if (repository.findByRequesterIdAndEventId(userId, eventId) != null) {
            throw new AlreadyExistsException("Request to participate on event already exist");
        }
        if (event.getParticipantLimit() != 0 && (event.getParticipantLimit() <= repository.countAllByEventIdAndStatus(eventId, Status.CONFIRMED))) {
            throw new ConflictException("The number of participants has reached the maximum value");
        }
        ParticipationRequestDto requestDto = ParticipationRequestDto.builder()
                .event(eventId)
                .requester(userId)
                .status((event.getParticipantLimit() == 0) || !event.getRequestModeration() ? Status.CONFIRMED : Status.PENDING)
                .created(LocalDateTime.now())
                .build();
        return mapper.toParticipationRequestDto(repository.save(mapper.toRequest(requestDto)));
    }

    @Override
    @Transactional
    public ParticipationRequestDto cancelUserRequest(Integer userId, Integer requestId) {
        userService.findById(userId);
        Request request = getRequest(requestId);
        if (!request.getRequester().getId().equals(userId)) {
            throw new ConflictException(String.format("You are not owner of request with id=%d", requestId));
        }
        if (!Status.PENDING.equals(request.getStatus())) {
            throw new ConflictException(String.format("Request with id=%d already not pending", requestId));
        }
        request.setStatus(Status.CANCELED);
        return mapper.toParticipationRequestDto(request);
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult updateParticipationRequestByUser(Integer userId, Integer eventId, EventRequestStatusUpdateRequest requestUpdate) {
        userService.findById(userId);
        Event event = eventService.findById(eventId);

        if (!List.of(Status.CONFIRMED, Status.REJECTED).contains(requestUpdate.getStatus())) {
            throw new IllegalArgumentException("Wrong status to confirm or reject requests " + requestUpdate.getStatus());
        } else if (Status.REJECTED.equals(requestUpdate.getStatus())) {
            List<Request> requests = repository.findAllByIdIn(requestUpdate.getRequestIds());
            if (requests.stream().anyMatch(r -> !Status.PENDING.equals(r.getStatus()))) {
                throw new ConflictException("All requests must have status PENDING");
            }
            requests.forEach(r -> r.setStatus(Status.REJECTED));
            return EventRequestStatusUpdateResult.builder()
                    .rejectedRequests(repository.findAllByIdIn(requestUpdate.getRequestIds())
                            .stream()
                            .map(mapper::toParticipationRequestDto)
                            .collect(Collectors.toList()))
                    .confirmedRequests(Collections.emptyList())
                    .build();
        }

        if (event.getParticipantLimit() == 0 || !event.getRequestModeration()) {
            List<Request> requests = repository.findAllByIdIn(requestUpdate.getRequestIds());
            requests.forEach(r -> r.setStatus(Status.CONFIRMED));
            return EventRequestStatusUpdateResult.builder()
                    .confirmedRequests(requests.stream()
                            .map(mapper::toParticipationRequestDto)
                            .collect(Collectors.toList()))
                    .rejectedRequests(Collections.emptyList())
                    .build();
        }

        Integer confirmedParticipantCount = repository.countAllByEventIdAndStatus(eventId, Status.CONFIRMED);

        if (event.getParticipantLimit() != 0 && confirmedParticipantCount >= event.getParticipantLimit()) {
            throw new ConflictException("The participant limit has been reached");
        }

        List<Request> requests = repository.findAllByIdIn(requestUpdate.getRequestIds());
        if (requests.stream().anyMatch(r -> !Status.PENDING.equals(r.getStatus()))) {
            throw new ConflictException("All requests must have status PENDING");
        }

        int vacancyCount = event.getParticipantLimit() - confirmedParticipantCount;

        if (vacancyCount >= requests.size()) {
            requests.forEach(r -> r.setStatus(Status.CONFIRMED));
            return EventRequestStatusUpdateResult.builder()
                    .confirmedRequests(requests.stream().map(mapper::toParticipationRequestDto).collect(Collectors.toList()))
                    .rejectedRequests(Collections.emptyList())
                    .build();
        } else {
            List<Request> confirm = requests.subList(0, vacancyCount);
            List<Request> reject = requests.subList(vacancyCount, requests.size());
            confirm.forEach(r -> r.setStatus(Status.CONFIRMED));
            reject.forEach(r -> r.setStatus(Status.REJECTED));
            return EventRequestStatusUpdateResult.builder()
                    .confirmedRequests(confirm.stream().map(mapper::toParticipationRequestDto).collect(Collectors.toList()))
                    .rejectedRequests(reject.stream().map(mapper::toParticipationRequestDto).collect(Collectors.toList()))
                    .build();
        }
    }

    private Request getRequest(Integer requestId) {
        return repository.findById(requestId)
                .orElseThrow(() -> new NotFoundException(String.format("Request with id=%d was not found", requestId)));
    }
}
