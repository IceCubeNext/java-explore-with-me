package ru.practicum.ewm.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class NewCommentDto {
    @NotBlank(message = "{text should not be empty}")
    @Size(min = 1, max = 1000, message = "{text size should be from 1 to 1000 letters}")
    private String text;
}
