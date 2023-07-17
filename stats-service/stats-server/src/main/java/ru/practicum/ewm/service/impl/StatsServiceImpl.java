package ru.practicum.ewm.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.repository.StatRepository;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl {
    private final StatRepository statRepository;
}
