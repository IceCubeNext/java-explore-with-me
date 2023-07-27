package ru.practicum.ewm.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.NewUserRequest;
import ru.practicum.ewm.dto.UserDto;
import ru.practicum.ewm.exceptions.NotFoundException;
import ru.practicum.ewm.mapper.UserMapper;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.repository.UserRepository;
import ru.practicum.ewm.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final UserMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public User findById(Integer userId) {
        return repository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id=%d was not found", userId)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getUsers(List<Integer> ids, Integer from, Integer size) {
        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size);
        if (ids == null || ids.isEmpty()) {
            return repository.findAll(page).map(mapper::toUserDto).getContent();
        }
        return repository.findAllByIdIn(ids, page).map(mapper::toUserDto).getContent();
    }

    @Override
    @Transactional
    public UserDto addUser(NewUserRequest userShortDto) {
        User user = mapper.toUser(userShortDto);
        return mapper.toUserDto(repository.save(user));
    }

    @Override
    @Transactional
    public void deleteUser(Integer userId) {
        findById(userId);
        repository.deleteById(userId);
    }
}
