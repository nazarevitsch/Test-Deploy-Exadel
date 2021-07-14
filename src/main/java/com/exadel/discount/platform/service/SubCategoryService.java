package com.exadel.discount.platform.service;

import com.exadel.discount.platform.exception.DeletedException;
import com.exadel.discount.platform.exception.NotFoundException;
import com.exadel.discount.platform.mapper.SubCategoryMapper;
import com.exadel.discount.platform.model.Category;
import com.exadel.discount.platform.model.SubCategory;
import com.exadel.discount.platform.model.dto.SubCategoryResponseDto;
import com.exadel.discount.platform.model.dto.update.SubCategoryBaseDto;
import com.exadel.discount.platform.repository.CategoryRepository;
import com.exadel.discount.platform.repository.SubCategoryRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SubCategoryService {

    private final SubCategoryRepository subCategoryRepository;
    private final SubCategoryMapper mapper;
    private final CategoryRepository categoryRepository;

    public List<SubCategoryResponseDto> getAllByCategoryId(UUID categoryId, boolean isDeleted) {
        return mapper.mapList(subCategoryRepository.findAllByCategoryIdAndDeleted(categoryId, isDeleted));
    }

    public SubCategoryResponseDto save(UUID categoryId, SubCategoryBaseDto subCategoryBaseDto) {
        Category category = categoryRepository.getById(categoryId);
        if (category.isDeleted()) {
            throw new DeletedException("Cannot create SubCategory with deleted Category");
        }

        SubCategory savedSubCategory = mapper.dtoToEntity(subCategoryBaseDto);
        savedSubCategory.setCategory(category);
        savedSubCategory = subCategoryRepository.save(savedSubCategory);
        return mapper.entityToResponseDto(savedSubCategory);
    }

    public SubCategoryResponseDto getByCategoryIdAndId(UUID categoryId, UUID id) {
        SubCategory subCategory = subCategoryRepository
                .findByCategoryIdAndId(categoryId, id)
                .orElseThrow(() -> new NotFoundException("SubCategory with id " + id + " in Category with id " + categoryId +
                        " was not found"));
        return mapper.entityToResponseDto(subCategory);
    }

    public SubCategoryResponseDto update(UUID categoryId, UUID id, SubCategoryBaseDto subCategoryBaseDto) {
        SubCategory subCategory = subCategoryRepository
                .findByCategoryIdAndId(categoryId, id)
                .orElseThrow(() -> new NotFoundException("SubCategory with id " + id + " in Category with id " + categoryId +
                        " was not found"));
        if (subCategory.isDeleted()) {
            throw new DeletedException("Cannot update deleted SubCategory with id" + id
            );
        }
        mapper.update(subCategoryBaseDto, subCategory);
        subCategory = subCategoryRepository.save(subCategory);
        return mapper.entityToResponseDto(subCategory);
    }

    public void toArchive(UUID categoryId, UUID id) {
        boolean exists = subCategoryRepository.existsByCategoryIdAndId(categoryId, id);
        if (exists) {
            subCategoryRepository.deleteById(id);
        } else {
            throw new NotFoundException("SubCategory with id" + id + " in Category with id " + categoryId +
                    " was not found");
        }
    }
}
