package ru.practicum.ewm.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.CompilationDto;
import ru.practicum.ewm.dto.EventShortDto;
import ru.practicum.ewm.dto.NewCompilationDto;
import ru.practicum.ewm.exceptions.NotFoundException;
import ru.practicum.ewm.mapper.CompilationMapper;
import ru.practicum.ewm.mapper.EventMapper;
import ru.practicum.ewm.model.Compilation;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.EventCompilation;
import ru.practicum.ewm.repository.CompilationRepository;
import ru.practicum.ewm.repository.EventCompilationRepository;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.service.CompilationService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventCompilationRepository eventCompilationRepository;
    private final EventRepository eventRepository;
    private final CompilationMapper compilationMapper;
    private final EventMapper eventMapper;

    @Override
    @Transactional(readOnly = true)
    public List<CompilationDto> getCompilations(Integer from, Integer size) {
        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size);
        Map<Compilation, List<Event>> map = eventCompilationRepository.findAll().stream()
                .collect(groupingBy())
        for (CompilationDto compilation: compilations) {
            List<Event> events = eventRepository.findAllByIdIn(
                                    eventCompilationRepository.findAllByCompilationId(compilation.getId())
                                        .stream()
                                        .map(EventCompilation::getEventId)
                                        .collect(Collectors.toList()));
            compilation.setEvents(events.stream().map(eventMapper::toEventShortDto).collect(Collectors.toList()));
        }
        return compilations;
    }

    @Override
    @Transactional(readOnly = true)
    public CompilationDto getCompilationById(Integer id) {
        return compilationMapper.toCompilationDto(getCompilation(id));
    }

    @Override
    @Transactional
    public CompilationDto addCompilation(NewCompilationDto compilationDto) {
        Compilation compilation = compilationMapper.toCompilation(compilationDto);
        List<EventShortDto>
        return compilationMapper.toCompilationDto(compilationRepository.save(compilation));
    }

    @Override
    @Transactional
    public CompilationDto updateCompilation(Integer id, NewCompilationDto compilationDto) {
        Compilation compilation = getCompilation(id);

    }

    @Override
    @Transactional
    public void deleteCompilation(Integer id);

    private Compilation getCompilation(Integer id) {
        return compilationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Compilation with id=%d not found", id)));
    }
}
