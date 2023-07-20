package ru.practicum.ewm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.ewm.model.enums.State;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ParticipationRequestDto {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String created;
    private Integer event;
    private Integer id;
    private Integer requester;
    private State status;
}
