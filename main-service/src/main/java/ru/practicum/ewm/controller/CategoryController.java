package ru.practicum.ewm.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.CategoryDto;
import ru.practicum.ewm.dto.NewCategoryDto;
import ru.practicum.ewm.service.CategoryService;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/categories")
    public List<CategoryDto> getCategories(@RequestParam(defaultValue = "0") Integer from,
                                           @RequestParam(defaultValue = "10") Integer size) {
        return categoryService.getCategories(from, size);
    }

    @GetMapping("/categories/{catId}")
    public CategoryDto getCategoryById(@PathVariable Integer catId) {
        return categoryService.getCategoryById(catId);
    }

    @PostMapping("/admin/categories")
    public CategoryDto addCategory(@RequestBody NewCategoryDto newCategoryDto) {
        return categoryService.addCategory(newCategoryDto);
    }

    @PatchMapping("/admin/categories/{catId}")
    public CategoryDto updateCategory(@PathVariable Integer catId,
                                      @RequestBody NewCategoryDto newCategoryDto) {
        return categoryService.updateCategory(catId, newCategoryDto);
    }

    @DeleteMapping("/admin/categories/{catId}")
    public void deleteCategory(@PathVariable Integer catId) {
        categoryService.deleteCategory(catId);
    }
}
