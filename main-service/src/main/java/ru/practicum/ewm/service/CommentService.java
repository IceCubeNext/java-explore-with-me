package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.CommentDto;
import ru.practicum.ewm.dto.NewCommentDto;

import java.util.List;

public interface CommentService {
    CommentDto getCommentById(Integer eventId, Integer comId);

    List<CommentDto> getCommentsByEventId(Integer eventId, String text, Integer from, Integer size);

    List<CommentDto> getCommentsByUserId(Integer userId, String text, Integer from, Integer size);

    CommentDto addComment(Integer userId, Integer eventId, NewCommentDto commentDto);

    CommentDto updateComment(Integer userId, Integer eventId, Integer comId, NewCommentDto commentDto);

    void deleteComment(Integer userId, Integer eventId, Integer comId);
    void deleteCommentByAdmin(Integer comId);
}
