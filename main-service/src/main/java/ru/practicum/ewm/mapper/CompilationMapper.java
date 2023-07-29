package ru.practicum.ewm.mapper;

import org.mapstruct.Mapper;
import ru.practicum.ewm.dto.CompilationDto;
import ru.practicum.ewm.dto.NewCompilationDto;
import ru.practicum.ewm.model.Compilation;

@Mapper(componentModel = "spring")
public interface CompilationMapper {

    CompilationDto toCompilationDto(Compilation compilation);

    Compilation toCompilation(NewCompilationDto compilationDto);
}
