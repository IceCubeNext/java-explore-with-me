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
    String start;
    @NotBlank
    String end;
    String uris;
    Boolean unique;
}