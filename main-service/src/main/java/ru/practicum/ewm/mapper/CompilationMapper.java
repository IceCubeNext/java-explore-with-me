package ru.practicum.ewm.mapper;

import org.mapstruct.Mapper;
import ru.practicum.ewm.dto.CompilationDto;
import ru.practicum.ewm.dto.NewCompilationDto;
import ru.practicum.ewm.model.Compilation;

import java.util.List;

@Mapper
public interface CompilationMapper {

    CompilationDto toCompilationDto(Compilation compilation);

    Compilation toCompilation(NewCompilationDto compilationDto);
}
