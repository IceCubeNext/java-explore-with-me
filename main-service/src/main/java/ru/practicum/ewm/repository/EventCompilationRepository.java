package ru.practicum.ewm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.model.EventCompilation;

import java.util.List;

public interface EventCompilationRepository extends JpaRepository<EventCompilation, Integer> {
    List<EventCompilation> findAllByCompilationId(Integer id);
}
