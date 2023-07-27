package ru.practicum.ewm.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.model.Request;
import ru.practicum.ewm.model.enums.Status;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Integer> {
    List<Request> findAllByIdIn(List<Integer> ids);
    List<Request> findAllByRequesterId(Integer userId);
    Page<Request> findAllByEventIdAndEventInitiatorId(Integer eventId, Integer userId, Pageable page);
    Request findByRequesterIdAndEventId(Integer userId, Integer eventId);
    Integer countAllByEventIdAndStatus(Integer id, Status status);
    List<Request> findAllByEventIdInAndStatus(List<Integer> ids, Status status);
}
