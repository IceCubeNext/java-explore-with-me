package ru.practicum.ewm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SearchEventParameters {
    private String ip;
    private String text;
    private List<Integer> categories;
    private Boolean paid;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String rangeStart;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String rangeEnd;
    private Boolean onlyAvailable;
    private String sort;
}
