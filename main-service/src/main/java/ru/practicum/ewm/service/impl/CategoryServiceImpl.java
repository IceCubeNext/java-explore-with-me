package ru.practicum.ewm.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.CategoryDto;
import ru.practicum.ewm.dto.NewCategoryDto;
import ru.practicum.ewm.exceptions.AlreadyExistsException;
import ru.practicum.ewm.exceptions.NotFoundException;
import ru.practicum.ewm.mapper.CategoryMapper;
import ru.practicum.ewm.model.Category;
import ru.practicum.ewm.repository.CategoryRepository;
import ru.practicum.ewm.service.CategoryService;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDto> getCategories(Integer from, Integer size) {
        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size);
        return categoryRepository.findAll(page).map(mapper::toCategoryDto).getContent();
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDto getCategoryById(Integer catId) {
        return mapper.toCategoryDto(findById(catId));
    }

    @Override
    @Transactional
    public CategoryDto addCategory(NewCategoryDto newCategoryDto) {
        if (categoryRepository.findFirstByName(newCategoryDto.getName()) != null) {
            throw new AlreadyExistsException(String.format("Category with name %s already exists", newCategoryDto.getName()));
        }
        Category category = mapper.toCategory(newCategoryDto);
        return mapper.toCategoryDto(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(Integer id, NewCategoryDto categoryDto) {
        Category category = findById(id);
        if (StringUtils.hasText(categoryDto.getName()) && !category.getName().equals(categoryDto.getName())) {
            category.setName(categoryDto.getName());
        }
        return mapper.toCategoryDto(category);
    }

    @Override
    @Transactional
    public void deleteCategory(Integer id) {
        Category category = findById(id);
        categoryRepository.delete(category);
    }

    @Override
    @Transactional
    public Category findById(Integer catId) {
        return categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException(String.format("Category with id=%d was not found", catId)));
    }
}
