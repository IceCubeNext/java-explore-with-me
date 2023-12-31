package ru.practicum.ewm.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    Page<Comment> findAllByEventIdAndTextContainingIgnoreCase(Integer eventId, String text, Pageable page);

    Page<Comment> findAllByUserIdAndTextContainingIgnoreCase(Integer userId, String text, Pageable page);

    List<Comment> findAllByEventIdIn(List<Integer> events);

    List<Comment> findAllByEventId(Integer eventId);
}
