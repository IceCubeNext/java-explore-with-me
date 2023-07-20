package ru.practicum.ewm.mapper;

import org.mapstruct.Mapper;
import ru.practicum.ewm.dto.UserDto;
import ru.practicum.ewm.dto.UserShortDto;
import ru.practicum.ewm.model.User;

@Mapper
public interface UserMapper {
    UserDto toUserDto(User user);
    User toUser(UserShortDto userShortDto);
}
