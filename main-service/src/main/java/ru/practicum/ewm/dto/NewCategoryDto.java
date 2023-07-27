package ru.practicum.ewm.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class NewCategoryDto {
    @NotBlank
    @Size(min = 1, max = 50, message = "{category name size should be from 1 to 50 letters}")
    private String name;
}
