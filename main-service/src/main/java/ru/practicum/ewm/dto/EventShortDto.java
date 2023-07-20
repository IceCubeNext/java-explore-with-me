package ru.practicum.ewm.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventShortDto {
    private Integer id;
    @NotBlank
    private String title;
    @NotBlank
    private String annotation;
    @NotNull
    private CategoryDto category;
    @NotNull
    private UserShortDto initiator;
    @NotNull
    private Boolean paid;
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String eventDate;
    private Integer confirmedRequests;
    private Integer views;
}
