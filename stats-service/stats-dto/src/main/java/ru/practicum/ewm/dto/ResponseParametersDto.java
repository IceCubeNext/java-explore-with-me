package ru.practicum.ewm.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.ewm.validation.StartBeforeEndDateValid;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@StartBeforeEndDateValid
public class ResponseParametersDto {
    @NotBlank
    private String start;
    @NotBlank
    private String end;
    private String uris;
    private Boolean unique;
}