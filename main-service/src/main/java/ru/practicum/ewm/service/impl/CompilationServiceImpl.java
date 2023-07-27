package ru.practicum.ewm.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.practicum.ewm.dto.CompilationDto;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

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
    public List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size) {
        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size);
        List<Compilation> compilations;

        if (pinned != null) {
            compilations = compilationRepository.findAllByPinned(pinned, page).getContent();
        } else {
            compilations = compilationRepository.findAll(page).getContent();
        }

        Map<Compilation, List<Event>> compilationEvents = eventCompilationRepository.findAllByCompilationIn(compilations)
                .stream()
                .collect(Collectors.groupingBy(
                                EventCompilation::getCompilation,
                                Collectors.mapping(EventCompilation::getEvent, toList())
                        )
                );

        List<CompilationDto> compilationDtoList = new ArrayList<>();

        for (Compilation compilation : compilations) {
            CompilationDto compilationDto = compilationMapper.toCompilationDto(compilation);
            if (compilationEvents.containsKey(compilation)) {
                compilationDto.setEvents(compilationEvents.get(compilation).stream().map(eventMapper::toEventShortDto).collect(toList()));
            } else {
                compilationDto.setEvents(Collections.emptyList());
            }
            compilationDtoList.add(compilationDto);
        }
        return compilationDtoList;
    }

    @Override
    @Transactional(readOnly = true)
    public CompilationDto getCompilationById(Integer id) {
        CompilationDto compilationDto = compilationMapper.toCompilationDto(getCompilation(id));
        return setEvents(compilationDto, id);
    }

    @Override
    @Transactional
    public CompilationDto addCompilation(NewCompilationDto compilationDto) {
        if (compilationDto.getPinned() == null) {
            compilationDto.setPinned(false);
        }
        Compilation compilation = compilationRepository.save(compilationMapper.toCompilation(compilationDto));
        if (compilationDto.getEvents() != null && !compilationDto.getEvents().isEmpty()) {
            updateEventCompilations(compilation, compilationDto.getEvents());
        }
        CompilationDto result = compilationMapper.toCompilationDto(compilation);
        return setEvents(result, compilation.getId());
    }

    @Override
    @Transactional
    public CompilationDto updateCompilation(Integer id, NewCompilationDto compilationDto) {
        Compilation compilation = getCompilation(id);
        if (StringUtils.hasText(compilationDto.getTitle()) && !compilationDto.getTitle().equals(compilation.getTitle())) {
            if (compilationDto.getTitle().length() > 50) {
                throw new IllegalArgumentException("compilation title size should be from 1 to 50 letters");
            }
            compilation.setTitle(compilationDto.getTitle());
        }
        if (compilationDto.getPinned() != compilation.getPinned()) {
            compilation.setPinned(compilationDto.getPinned());
        }
        if (compilationDto.getEvents() != null) {
            eventCompilationRepository.deleteAllByCompilation(compilation);
            updateEventCompilations(compilation, compilationDto.getEvents());
        }
        CompilationDto result = compilationMapper.toCompilationDto(compilation);
        return setEvents(result, compilation.getId());
    }

    @Override
    @Transactional
    public void deleteCompilation(Integer id) {
        Compilation compilation = getCompilation(id);
        compilationRepository.delete(compilation);
    }

    private void updateEventCompilations(Compilation compilation, List<Integer> eventIds) {
        List<Event> events = eventRepository.findAllByIdIn(eventIds);
        List<EventCompilation> eventCompilations = new ArrayList<>();
        for (Event event : events) {
            EventCompilation ec = new EventCompilation();
            ec.setCompilation(compilation);
            ec.setEvent(event);
            eventCompilations.add(ec);
        }
        eventCompilationRepository.saveAll(eventCompilations);
    }

    private CompilationDto setEvents(CompilationDto compilationDto, Integer compId) {
        compilationDto.setEvents(eventCompilationRepository.findAllByCompilationId(compId).stream()
                .map(EventCompilation::getEvent)
                .map(eventMapper::toEventShortDto)
                .collect(toList()));
        return compilationDto;
    }

    private Compilation getCompilation(Integer id) {
        return compilationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Compilation with id=%d not found", id)));
    }
}
