package ru.practicum.ewm.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.practicum.ewm.dto.CommentDto;
import ru.practicum.ewm.dto.NewCommentDto;
import ru.practicum.ewm.exceptions.ConflictException;
import ru.practicum.ewm.exceptions.NotFoundException;
import ru.practicum.ewm.mapper.CommentMapper;
import ru.practicum.ewm.model.Comment;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.model.enums.Status;
import ru.practicum.ewm.repository.CommentRepository;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.repository.UserRepository;
import ru.practicum.ewm.service.CommentService;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository repository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CommentMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public CommentDto getCommentById(Integer eventId, Integer comId) {
        Comment comment = findById(comId);
        Event event = getEvent(eventId);
        if (!Status.PUBLISHED.equals(event.getState())) {
            throw new NotFoundException(String.format("Event with id=%d was not found", eventId));
        }
        return mapper.toDto(comment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> getCommentsByEventId(Integer eventId, String text, Integer from, Integer size) {
        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size);
        getEvent(eventId);
        return repository.findAllByEventIdAndTextContainingIgnoreCase(eventId, text, page).map(mapper::toDto).getContent();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> getCommentsByUserId(Integer userId, String text, Integer from, Integer size) {
        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size);
        getUser(userId);
        return repository.findAllByUserIdAndTextContainingIgnoreCase(userId, text, page).map(mapper::toDto).getContent();
    }

    @Override
    @Transactional
    public CommentDto addComment(Integer userId, Integer eventId, NewCommentDto commentDto) {
        User user = getUser(userId);
        Event event = getEvent(eventId);
        if (!Status.PUBLISHED.equals(event.getState())) {
            throw new NotFoundException(String.format("Event with id=%d was not found", eventId));
        }
        Comment comment = new Comment();
        comment.setEvent(event);
        comment.setUser(user);
        comment.setText(commentDto.getText());
        return mapper.toDto(repository.save(comment));
    }

    @Override
    @Transactional
    public CommentDto updateComment(Integer userId, Integer eventId, Integer comId, NewCommentDto commentDto) {
        getUser(userId);
        Event event = getEvent(eventId);
        Comment comment = findById(comId);
        if (!Status.PUBLISHED.equals(event.getState())) {
            throw new NotFoundException(String.format("Event with id=%d was not found", eventId));
        }
        if (!Objects.equals(comment.getUser().getId(), userId)) {
            throw new ConflictException(String.format("User with id=%d is not owner of comment with id=%d", userId, comId));
        }

        if (StringUtils.hasText(commentDto.getText()) && !commentDto.getText().equals(comment.getText())) {
            comment.setText(commentDto.getText());
        }
        return mapper.toDto(comment);
    }

    @Override
    @Transactional
    public void deleteComment(Integer userId, Integer eventId, Integer comId) {
        getUser(userId);
        Event event = getEvent(eventId);
        Comment comment = findById(comId);
        if (!Status.PUBLISHED.equals(event.getState())) {
            throw new NotFoundException(String.format("Event with id=%d was not found", eventId));
        }
        if (!Objects.equals(comment.getUser().getId(), userId)) {
            throw new ConflictException(String.format("User with id=%d is not owner of comment with id=%d", userId, comId));
        }
        repository.deleteById(comId);
    }

    @Override
    @Transactional
    public void deleteCommentByAdmin(Integer comId) {
        findById(comId);
        repository.deleteById(comId);
    }

    private Comment findById(Integer comId) {
        return repository.findById(comId)
                .orElseThrow(() -> new NotFoundException(String.format("Comment with id=%d was not found", comId)));
    }

    private User getUser(Integer userId) {
        return userRepository.findById(userId).orElseThrow(()
                -> new NotFoundException(String.format("User with id=%d was not found", userId)));
    }

    private Event getEvent(Integer eventId) {
        return eventRepository.findById(eventId).orElseThrow(()
                -> new NotFoundException(String.format("Event with id=%d was not found", eventId)));
    }
}
