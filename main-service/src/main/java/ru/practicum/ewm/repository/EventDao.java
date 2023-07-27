package ru.practicum.ewm.repository;

import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.enums.Status;

import java.time.LocalDateTime;
import java.util.List;

public interface EventDao {
    List<Event> findByAdmin(List<Integer> userIds, List<Integer> categories, List<Status> states, LocalDateTime start, LocalDateTime end, Integer from, Integer size);

    List<Event> findByUser(String text, List<Integer> categories, Boolean paid, LocalDateTime start, LocalDateTime end, Integer from, Integer size);
}
