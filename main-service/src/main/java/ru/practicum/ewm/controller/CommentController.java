package ru.practicum.ewm.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.CommentDto;
import ru.practicum.ewm.dto.NewCommentDto;
import ru.practicum.ewm.service.CommentService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @GetMapping("events/{eventId}/comments/{comId}")
    public CommentDto getCommentById(@PathVariable Integer eventId,
                                     @PathVariable Integer comId) {
        log.info("Get comment with id={}, eventId={}", comId, eventId);
        return commentService.getCommentById(eventId, comId);
    }

    @GetMapping("events/{eventId}/comments")
    public List<CommentDto> getCommentsByEventId(@PathVariable Integer eventId,
                                                 @RequestParam(defaultValue = "") String text,
                                                 @RequestParam(defaultValue = "0") Integer from,
                                                 @RequestParam(defaultValue = "10") Integer size) {
        log.info("Get comments from event with id={}, text={}", eventId, text);
        return commentService.getCommentsByEventId(eventId, text, from, size);
    }

    @GetMapping("/admin/users/{userId}/comments")
    public List<CommentDto> getCommentsByUserId(@PathVariable Integer userId,
                                                @RequestParam(defaultValue = "") String text,
                                                @RequestParam(defaultValue = "0") Integer from,
                                                @RequestParam(defaultValue = "10") Integer size) {
        log.info("Get comments from user with id={}, text={}", userId, text);
        return commentService.getCommentsByUserId(userId, text, from, size);
    }

    @PostMapping("events/{eventId}/comments")
    @ResponseStatus(code = HttpStatus.CREATED)
    public CommentDto addComment(@PathVariable Integer eventId,
                                 @RequestParam Integer userId,
                                 @Valid @RequestBody NewCommentDto commentDto) {
        log.info("Post comment from user with id={}, under event with id={}, dto={}", userId, eventId, commentDto);
        return commentService.addComment(userId, eventId, commentDto);
    }

    @PatchMapping("events/{eventId}/comments/{comId}")
    public CommentDto updateComment(@PathVariable Integer eventId,
                                    @PathVariable Integer comId,
                                    @RequestParam Integer userId,
                                    @Valid @RequestBody NewCommentDto commentDto) {
        log.info("Patch comment with id={} from user with id={}, under event with id={}, dto={}", comId, userId, eventId, commentDto);
        return commentService.updateComment(userId, eventId, comId, commentDto);
    }

    @DeleteMapping("events/{eventId}/comments/{comId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Integer eventId,
                              @PathVariable Integer comId,
                              @RequestParam Integer userId) {
        log.info("Delete comment with id={} from user with id={}, under event with id={}", comId, userId, eventId);
        commentService.deleteComment(userId, eventId, comId);
    }

    @DeleteMapping("admin/comments/{comId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteCommentByAdmin(@RequestParam Integer comId) {
        log.info("Delete comment with id={} by admin", comId);
        commentService.deleteCommentByAdmin(comId);
    }
}
