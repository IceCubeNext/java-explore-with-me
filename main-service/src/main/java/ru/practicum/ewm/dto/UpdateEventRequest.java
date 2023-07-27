package ru.practicum.ewm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.ewm.exceptions.EventDateValid;
import ru.practicum.ewm.model.enums.StateAction;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UpdateEventRequest {
    @Size(min = 3, max = 120, message = "{title size should be from 20 to 7000 letters}")
    private String title;
    @Size(min = 20, max = 7000, message = "{description size should be from 20 to 7000 letters}")
    private String description;
    @Size(min = 20, max = 2000, message = "{annotation size should be from 20 to 2000 letters}")
    private String annotation;
    @EventDateValid
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private Integer category;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private StateAction stateAction;
}
