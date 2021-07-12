package com.exadel.discount.platform.service;

import com.exadel.discount.platform.exception.DeletedException;
import com.exadel.discount.platform.exception.NotFoundException;
import com.exadel.discount.platform.mapper.CategoryMapper;
import com.exadel.discount.platform.model.Category;
import com.exadel.discount.platform.model.dto.CategoryDto;
import com.exadel.discount.platform.model.dto.CategoryResponseDto;
import com.exadel.discount.platform.repository.CategoryRepository;
import com.exadel.discount.platform.repository.SubCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Transactional
@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final CategoryMapper mapper;

    public List<CategoryResponseDto> getAll(boolean isDeleted) {
        return mapper.mapList(categoryRepository.findAllByDeleted(isDeleted));
    }

    public CategoryResponseDto save(CategoryDto categoryDto) {
        Category category = categoryRepository.save(mapper.dtoToEntity(categoryDto));
        return mapper.entityToResponseDto(category);
    }

    public CategoryResponseDto getById(UUID id) {
        Category category = categoryRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Category with id " + id +
                        " was not found"));
        return mapper.entityToResponseDto(category);
    }

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

    public void toArchive(UUID id) {
            boolean exists = categoryRepository.existsById(id);
            if (exists){
                categoryRepository.deleteById(id);
                subCategoryRepository.deleteAllByCategoryId(id);
                return;
            }
            throw new NotFoundException("Category with id" + id + " was not found");
        }
}
