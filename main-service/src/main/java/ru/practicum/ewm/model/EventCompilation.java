package ru.practicum.ewm.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "event_compilation")
public class EventCompilation {
    @Id
    private Integer compilationId;
    @Id
    private Integer eventId;
}
