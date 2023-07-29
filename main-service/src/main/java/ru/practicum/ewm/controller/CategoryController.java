package ru.practicum.ewm.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.CategoryDto;
import ru.practicum.ewm.dto.NewCategoryDto;
import ru.practicum.ewm.service.CategoryService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/categories")
    public List<CategoryDto> getCategories(@RequestParam(defaultValue = "0") Integer from,
                                           @RequestParam(defaultValue = "10") Integer size) {
        log.info("Get categories from={} size={}", from, size);
        return categoryService.getCategories(from, size);
    }

    @GetMapping("/categories/{catId}")
    public CategoryDto getCategoryById(@PathVariable Integer catId) {
        log.info("Get category with id={}", catId);
        return categoryService.getCategoryById(catId);
    }

    @PostMapping("/admin/categories")
    @ResponseStatus(code = HttpStatus.CREATED)
    public CategoryDto addCategory(@Valid @RequestBody NewCategoryDto newCategoryDto) {
        log.info("Post category={}", newCategoryDto);
        return categoryService.addCategory(newCategoryDto);
    }

    @PatchMapping("/admin/categories/{catId}")
    public CategoryDto updateCategory(@PathVariable Integer catId,
                                      @Valid @RequestBody NewCategoryDto newCategoryDto) {
        log.info("Patch category with id={}, category={}", catId, newCategoryDto);
        return categoryService.updateCategory(catId, newCategoryDto);
    }

    @DeleteMapping("/admin/categories/{catId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Integer catId) {
        log.info("Delete category with id={}", catId);
        categoryService.deleteCategory(catId);
    }
}
