package ru.practicum.ewm.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.CompilationDto;
import ru.practicum.ewm.dto.NewCompilationDto;
import ru.practicum.ewm.service.CompilationService;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
public class CompilationController {
    private final CompilationService compilationService;

    @GetMapping("/compilations")
    public List<CompilationDto> getCompilations(@RequestParam(defaultValue = "0") Integer from,
                                                @RequestParam(defaultValue = "10") Integer size) {
        return compilationService.getCompilations(from, size);
    }

    @GetMapping("/compilations/{compId}")
    public CompilationDto getCompilationById(@PathVariable Integer compId) {
        return compilationService.getCompilationById(compId);
    }

    @PostMapping("/admin/compilations")
    public CompilationDto addCompilation(@RequestBody NewCompilationDto newCompilationDto) {
        return compilationService.addCompilation(newCompilationDto);
    }

    @PatchMapping("/admin/compilations/{compId}")
    public CompilationDto updateCompilation(@PathVariable Integer compId,
                                            @RequestBody NewCompilationDto compilationDto) {
        return compilationService.updateCompilation(compId, compilationDto);
    }

    @DeleteMapping("/admin/compilations/{compId}")
    public void deleteCompilation(@PathVariable Integer compId) {
        compilationService.deleteCompilation(compId);
    }
}
