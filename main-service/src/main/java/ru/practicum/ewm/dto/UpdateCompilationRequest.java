package ru.practicum.ewm.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UpdateCompilationRequest {
    @NotNull
    private List<Integer> events;
    @NotNull
    private Boolean pinned;
    @Size(min = 1, max = 50, message = "{title size should be from 1 to 50 letters}")
    private String title;
}
