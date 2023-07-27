package ru.practicum.ewm.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.model.Event;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer>, EventDao {

    List<Event> findAllByIdIn(List<Integer> ids);

    Page<Event> findAllByInitiatorId(Integer id, Pageable page);

    Event findFirstByCategoryId(Integer catId);
}
