package ru.practicum.ewm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.ewm.exceptions.EventDateValid;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class NewEventDto {
    @NotBlank
    @Size(min = 3, max = 120, message = "{title size should be from 20 to 7000 letters}")
    private String title;
    @NotBlank
    @Size(min = 20, max = 2000, message = "{annotation size should be from 20 to 2000 letters}")
    private String annotation;
    @NotBlank
    @Size(min = 20, max = 7000, message = "{description size should be from 20 to 7000 letters}")
    private String description;
    @NotNull
    private Integer category;
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @EventDateValid
    private LocalDateTime eventDate;
    @NotNull
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
}
