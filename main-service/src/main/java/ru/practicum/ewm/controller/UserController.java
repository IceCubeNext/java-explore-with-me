package ru.practicum.ewm.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.NewUserRequest;
import ru.practicum.ewm.dto.UserDto;
import ru.practicum.ewm.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/admin/users")
    public List<UserDto> getUsers(@RequestParam(required = false) List<Integer> ids,
                                  @RequestParam(defaultValue = "0") Integer from,
                                  @RequestParam(defaultValue = "10") Integer size) {
        log.info("Get users by ids={}, from={}, size={}", ids, from, size);
        return userService.getUsers(ids, from, size);
    }

    @PostMapping("/admin/users")
    @ResponseStatus(code = HttpStatus.CREATED)
    public UserDto addUsers(@Valid @RequestBody NewUserRequest userDto) {
        log.info("Post user={} by admin", userDto);
        return userService.addUser(userDto);
    }

    @DeleteMapping("/admin/users/{userId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void addUsers(@PathVariable Integer userId) {
        log.info("Delete user with id={} by admin", userId);
        userService.deleteUser(userId);
    }
}
