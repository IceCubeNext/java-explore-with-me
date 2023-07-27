package ru.practicum.ewm.dto;

import lombok.*;
import ru.practicum.ewm.model.enums.Status;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EventRequestStatusUpdateRequest {
    @NotNull
    private List<Integer> requestIds;
    @NotNull
    private Status status;
}
