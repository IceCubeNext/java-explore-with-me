package ru.practicum.ewm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.model.Event;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Integer> {
    List<Event> findAllByIdIn(List<Integer> ids);
}
