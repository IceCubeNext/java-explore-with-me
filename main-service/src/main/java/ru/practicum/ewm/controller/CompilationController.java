package ru.practicum.ewm.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.CompilationDto;
import ru.practicum.ewm.dto.NewCompilationDto;
import ru.practicum.ewm.service.CompilationService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
public class CompilationController {
    private final CompilationService compilationService;

    @GetMapping("/compilations")
    public List<CompilationDto> getCompilations(@RequestParam(required = false) Boolean pinned,
                                                @RequestParam(defaultValue = "0") Integer from,
                                                @RequestParam(defaultValue = "10") Integer size) {
        log.info("Get compilations from={}, size={}, pinned={}", from, size, pinned);
        return compilationService.getCompilations(pinned, from, size);
    }

    @GetMapping("/compilations/{compId}")
    public CompilationDto getCompilationById(@PathVariable Integer compId) {
        log.info("Get compilation with id={}", compId);
        return compilationService.getCompilationById(compId);
    }

    @PostMapping("/admin/compilations")
    @ResponseStatus(code = HttpStatus.CREATED)
    public CompilationDto addCompilation(@Valid @RequestBody NewCompilationDto newCompilationDto) {
        log.info("Post compilation={}", newCompilationDto);
        return compilationService.addCompilation(newCompilationDto);
    }

    @PatchMapping("/admin/compilations/{compId}")
    public CompilationDto updateCompilation(@PathVariable Integer compId,
                                            @RequestBody NewCompilationDto compilationDto) {
        log.info("Patch compilation with id={} compilation={}", compId, compilationDto);
        return compilationService.updateCompilation(compId, compilationDto);
    }

    @DeleteMapping("/admin/compilations/{compId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable Integer compId) {
        log.info("Delete compilation with id={}", compId);
        compilationService.deleteCompilation(compId);
    }
}
