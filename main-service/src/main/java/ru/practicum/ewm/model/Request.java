package ru.practicum.ewm.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.ewm.model.enums.Status;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "request")
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Event event;
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private User requester;
    @Column
    @Enumerated(EnumType.STRING)
    private Status status;
    @Column
    private LocalDateTime created;
}
