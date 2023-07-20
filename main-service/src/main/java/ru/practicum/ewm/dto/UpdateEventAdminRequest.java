package ru.practicum.ewm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.ewm.model.enums.StateAdminAction;

import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEventAdminRequest {
    @Size(min = 3, max = 120, message = "{title size should be from 20 to 7000 letters}")
    private String title;
    @Size(min = 20, max = 7000, message = "{description size should be from 20 to 7000 letters}")
    private String description;
    @Size(min = 20, max = 2000, message = "{annotation size should be from 20 to 2000 letters}")
    private String annotation;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String eventDate;
    private Integer category;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private StateAdminAction stateAdminAction;
}
