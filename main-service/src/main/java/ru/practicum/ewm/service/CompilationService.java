package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.CompilationDto;
import ru.practicum.ewm.dto.NewCompilationDto;

import java.util.List;

public interface CompilationService {
    List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size);

    CompilationDto getCompilationById(Integer id);

    CompilationDto addCompilation(NewCompilationDto compilationDto);

    CompilationDto updateCompilation(Integer id, NewCompilationDto compilationDto);

    void deleteCompilation(Integer id);
}
