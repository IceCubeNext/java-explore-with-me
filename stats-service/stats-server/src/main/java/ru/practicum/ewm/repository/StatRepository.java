package ru.practicum.ewm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.model.Hit;
import ru.practicum.ewm.model.StatView;

import java.time.LocalDateTime;
import java.util.List;

public interface StatRepository extends JpaRepository<Hit, Long> {
    @Query(value = "select hit.app, hit.uri, count(distinct(hit.ip)) as hits from Hit as hit " +
            "where hit.timestamp between ?1 and ?2 " +
            "and hit.uri IN (?3) " +
            "group by hit.uri, hit.app " +
            "order by count(distinct(hit.ip)) desc")
    List<StatView> findAllByTimestampBetweenAndUriInUniqueIp(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query(value = "select hit.app, hit.uri, count(hit.ip) as hits from Hit as hit " +
            "where hit.timestamp between ?1 and ?2 " +
            "and hit.uri IN (?3) " +
            "group by hit.uri, hit.app " +
            "order by count(hit.ip) desc")
    List<StatView> findAllByTimestampBetweenAndUriIn(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query(value = "select hit.app, hit.uri, count(distinct(hit.ip)) as hits from Hit as hit " +
            "where hit.timestamp between ?1 and ?2 " +
            "group by hit.uri, hit.app " +
            "order by count(distinct(hit.ip)) desc")
    List<StatView> findAllByTimestampBetweenUniqueIp(LocalDateTime start, LocalDateTime end);

    @Query(value = "select hit.app, hit.uri, count(hit.ip) as hits from Hit as hit " +
            "where hit.timestamp between ?1 and ?2 " +
            "group by hit.uri, hit.app " +
            "order by count(hit.ip) desc")
    List<StatView> findAllByTimestampBetween(LocalDateTime start, LocalDateTime end);
}
