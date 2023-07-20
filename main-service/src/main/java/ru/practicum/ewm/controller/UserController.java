package ru.practicum.ewm.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.UserDto;
import ru.practicum.ewm.dto.UserShortDto;
import ru.practicum.ewm.service.UserService;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/admin/users")
    public List<UserDto> getUsers(@RequestParam List<Integer> ids,
                                  @RequestParam Integer from,
                                  @RequestParam Integer size) {
        return userService.getUsers(ids, from, size);
    }

    @PostMapping("/admin/users")
    public UserDto addUsers(@RequestBody UserShortDto userDto) {
        return userService.addUser(userDto);
    }

    @DeleteMapping("/admin/users/{userId}")
    public void addUsers(@PathVariable Integer userId) {
        userService.deleteUser(userId);
    }
}
