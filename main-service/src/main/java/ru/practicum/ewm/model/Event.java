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
@Table(name = "event")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private String title;
    @Column
    private String annotation;
    @Column
    private String description;
    @Column
    private Boolean paid;
    @Column
    private LocalDateTime createdOn;
    @Column
    private LocalDateTime eventDate;
    @Column
    private LocalDateTime publishedOn;
    @Column
    private Integer participantLimit;
    @Column
    private Boolean requestModeration;
    @Column
    @Enumerated(EnumType.STRING)
    private Status state;
    @Column
    private Integer views;
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Category category;
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private User initiator;
    @Column
    private Float lat;
    @Column
    private Float lon;
}
