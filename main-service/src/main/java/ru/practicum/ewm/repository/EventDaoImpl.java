package ru.practicum.ewm.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.enums.Status;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
@AllArgsConstructor
public class EventDaoImpl implements EventDao {
    EntityManager em;

    @Override
    public List<Event> findByAdmin(List<Integer> userIds, List<Integer> categories, List<Status> states, LocalDateTime start, LocalDateTime end, Integer from, Integer size) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Event> cq = cb.createQuery(Event.class);
        Root<Event> event = cq.from(Event.class);
        List<Predicate> predicates = new ArrayList<>();

        if (userIds != null && !userIds.isEmpty()) {
            predicates.add(event.get("initiator").get("id").in(userIds));
        }
        if (categories != null && !categories.isEmpty()) {
            predicates.add(event.get("category").get("id").in(categories));
        }
        if (states != null && !states.isEmpty()) {
            predicates.add(event.get("state").in(states));
        }
        if (start != null) {
            predicates.add(cb.greaterThanOrEqualTo(event.get("eventDate"), start));
        }
        if (end != null) {
            predicates.add(cb.lessThanOrEqualTo(event.get("eventDate"), end));
        }
        Predicate finalPredicate = cb.and(predicates.toArray(new Predicate[0]));

        return em.createQuery(cq.where(finalPredicate))
                .setFirstResult(from)
                .setMaxResults(size)
                .getResultList();
    }

    @Override
    public List<Event> findByUser(String text, List<Integer> categories, Boolean paid, LocalDateTime start, LocalDateTime end, Integer from, Integer size) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Event> cq = cb.createQuery(Event.class);
        Root<Event> event = cq.from(Event.class);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(event.get("state"), Status.PUBLISHED));
        if (text != null && !text.isEmpty()) {
            predicates.add(
                    cb.or(
                            cb.like(cb.upper(event.get("annotation")), "%" + text.toUpperCase() + "%"),
                            cb.like(cb.upper(event.get("description")), "%" + text.toUpperCase() + "%")
                    ));
        }
        if (categories != null && !categories.isEmpty()) {
            predicates.add(event.get("category").get("id").in(categories));
        }
        if (paid != null) {
            predicates.add(cb.equal(event.get("paid"), paid));
        }

        predicates.add(cb.greaterThanOrEqualTo(event.get("eventDate"), Objects.requireNonNullElseGet(start, LocalDateTime::now)));

        if (end != null) {
            predicates.add(cb.lessThanOrEqualTo(event.get("eventDate"), end));
        }

        Predicate finalPredicate = cb.and(predicates.toArray(new Predicate[0]));
        return em.createQuery(cq.where(finalPredicate))
                .setFirstResult(from)
                .setMaxResults(size)
                .getResultList();
    }
}
