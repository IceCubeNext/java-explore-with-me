package ru.practicum.ewm.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCompilationRequest {
    private List<Integer> events;
    private Boolean pinned;
    @Size(min = 1, max = 50, message = "{title size should be from 1 to 50 letters}")
    private String title;
}
