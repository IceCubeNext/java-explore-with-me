package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.NewUserRequest;
import ru.practicum.ewm.dto.UserDto;
import ru.practicum.ewm.model.User;

import java.util.List;

public interface UserService {
    List<UserDto> getUsers(List<Integer> ids, Integer from, Integer size);

    User findById(Integer id);

    UserDto addUser(NewUserRequest userShortDto);

    void deleteUser(Integer userId);
}
