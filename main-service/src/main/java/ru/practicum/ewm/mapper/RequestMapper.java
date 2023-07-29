package ru.practicum.ewm.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.ewm.dto.ParticipationRequestDto;
import ru.practicum.ewm.model.Request;
import ru.practicum.ewm.service.EventService;
import ru.practicum.ewm.service.UserService;

@Mapper(componentModel = "spring", uses = {EventService.class, UserService.class})
public interface RequestMapper {
    @Mapping(source = "event.id", target = "event")
    @Mapping(source = "requester.id", target = "requester")
    ParticipationRequestDto toParticipationRequestDto(Request request);

    @Mapping(source = "event", target = "event")
    @Mapping(source = "requester", target = "requester")
    Request toRequest(ParticipationRequestDto participationRequestDto);
}
