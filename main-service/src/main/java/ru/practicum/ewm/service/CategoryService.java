package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.CategoryDto;
import ru.practicum.ewm.dto.NewCategoryDto;

import java.util.List;

public interface CategoryService {
    List<CategoryDto> getCategories(Integer from, Integer size);

    CategoryDto getCategoryById(Integer id);

    CategoryDto addCategory(NewCategoryDto newCategoryDto);

    CategoryDto updateCategory(Integer id, NewCategoryDto categoryDto);

    void deleteCategory(Integer id);
}
