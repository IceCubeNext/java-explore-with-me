package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.dto.ParticipationRequestDto;

import java.util.List;

public interface RequestService {
    List<ParticipationRequestDto> getUserRequests(Integer userId);

    List<ParticipationRequestDto> getParticipationRequestByUserId(Integer userId, Integer eventId, Integer from, Integer size);

    ParticipationRequestDto addUserRequest(Integer userId, Integer eventId);

    ParticipationRequestDto cancelUserRequest(Integer userId, Integer requestId);

    EventRequestStatusUpdateResult updateParticipationRequestByUser(Integer userId, Integer eventId, EventRequestStatusUpdateRequest request);
}
