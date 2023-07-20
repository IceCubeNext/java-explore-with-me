package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.UserDto;
import ru.practicum.ewm.dto.UserShortDto;

import java.util.List;

public interface UserService {
    List<UserDto> getUsers(List<Integer> ids, Integer from, Integer size);
    UserDto addUser(UserShortDto userShortDto);
    void deleteUser(Integer userId);
}
