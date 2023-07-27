package ru.practicum.ewm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.ewm.model.enums.Status;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SearchEventParametersAdmin {
    private List<Integer> userIds;
    private List<Status> states;
    private List<Integer> categories;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String rangeStart;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String rangeEnd;
}
