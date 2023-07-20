package ru.practicum.ewm.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewUserRequest {
    @Email(message = "{email incorrect}")
    @Size(min = 6, max = 254, message = "{email size should be from 6 to 254 letters}")
    private String email;
    @NotBlank
    @Size(min = 2, max = 250, message = "{name size should be from 2 to 250 letters}")
    private String name;
}
