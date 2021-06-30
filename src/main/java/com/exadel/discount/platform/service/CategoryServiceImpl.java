package com.exadel.discount.platform.service;

import com.exadel.discount.platform.exception.DeletedException;
import com.exadel.discount.platform.exception.NotFoundException;
import com.exadel.discount.platform.mapper.CategoryMapper;
import com.exadel.discount.platform.model.Category;
import com.exadel.discount.platform.model.dto.CategoryDto;
import com.exadel.discount.platform.model.dto.CategoryResponseDto;
import com.exadel.discount.platform.repository.CategoryRepository;
import com.exadel.discount.platform.service.interfaces.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper mapper;

    @Override
    public List<CategoryResponseDto> getAll() {
        return mapper.mapList(categoryRepository.findAll(), CategoryResponseDto.class);
    }

    @Override
    public CategoryResponseDto save(CategoryDto categoryDto) {
        Category category = categoryRepository.save(mapper.dtoToEntity(categoryDto));
        return mapper.entityToResponseDto(category);
    }

    @Override
    public CategoryResponseDto getById(UUID id) {
        Category category = categoryRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Category with id " + id +
                        " was not found"));
        return mapper.entityToResponseDto(category);
    }

    @Override
    public CategoryResponseDto update(UUID id, CategoryDto categoryDto) {
        Category category = categoryRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Category with id " + id +
                        " was not found"));

        if (category.isDeleted()) {
            throw new DeletedException("Cannot update deleted Category with id" + id
            );
        }
        mapper.update(categoryDto, category);
        category = categoryRepository.save(category);
        return mapper.entityToResponseDto(category);
    }

    @Override
    public void toArchive(UUID id) {
        boolean exists = categoryRepository.existsById(id);
        if (exists){
            categoryRepository.deleteById(id);
            return;
        }
            throw new NotFoundException("Category with id" + id + " was not found");
    }
}
