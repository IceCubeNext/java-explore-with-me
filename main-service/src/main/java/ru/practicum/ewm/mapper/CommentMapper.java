package ru.practicum.ewm.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.ewm.dto.CommentDto;
import ru.practicum.ewm.model.Comment;
import ru.practicum.ewm.service.EventService;
import ru.practicum.ewm.service.UserService;

@Mapper(componentModel = "spring", uses = {EventService.class, UserService.class})
public interface CommentMapper {
    @Mapping(source = "event.id", target = "eventId")
    @Mapping(source = "user.id", target = "userId")
    CommentDto toDto(Comment comment);
}
