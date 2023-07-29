package ru.practicum.ewm.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CategoryDto {
    @NotNull
    private Integer id;
    @NotBlank
    @Size(min = 1, max = 50, message = "{name size should be from 1 to 50 letters}")
    private String name;
}
