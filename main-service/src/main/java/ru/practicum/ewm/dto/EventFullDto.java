package ru.practicum.ewm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.ewm.model.enums.State;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventFullDto {
    private Integer id;
    @NotNull
    private String title;
    @NotBlank
    private String annotation;
    @NotNull
    private CategoryDto category;
    @NotNull
    private UserShortDto initiator;
    @NotNull
    private Location location;
    @NotNull
    private Boolean paid;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String createdOn;
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String eventDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String publishedOn;
    private String description;
    private Integer participantLimit;
    private Integer confirmedRequests;
    private Boolean requestModeration;
    private State state;
    private Integer views;
}
