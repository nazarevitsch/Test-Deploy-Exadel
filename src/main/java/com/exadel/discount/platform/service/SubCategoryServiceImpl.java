package com.exadel.discount.platform.service;



import com.exadel.discount.platform.exception.DeletedException;
import com.exadel.discount.platform.exception.NotFoundException;
import com.exadel.discount.platform.mapper.SubCategoryMapper;
import com.exadel.discount.platform.model.Category;
import com.exadel.discount.platform.model.SubCategory;
import com.exadel.discount.platform.model.dto.SubCategoryDto;
import com.exadel.discount.platform.model.dto.SubCategoryResponseDto;
import com.exadel.discount.platform.model.dto.update.SubCategoryBaseDto;
import com.exadel.discount.platform.repository.CategoryRepository;
import com.exadel.discount.platform.repository.SubCategoryRepository;
import com.exadel.discount.platform.service.interfaces.SubCategoryService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class SubCategoryServiceImpl implements SubCategoryService {

    private final SubCategoryRepository subCategoryRepository;
    private final SubCategoryMapper mapper;
    private final CategoryRepository categoryRepository;

    @Override
    public List<SubCategoryResponseDto> getAll(boolean isDeleted) {
        return mapper.mapList(subCategoryRepository.findAllByDeleted(isDeleted));
    }

    @Override
    public SubCategoryResponseDto save(SubCategoryDto subCategoryDto) {
        Category category = categoryRepository.getById(subCategoryDto.getCategoryId());
        if (category.isDeleted()) {
            throw new DeletedException("Cannot create SubCategory with deleted Category");
        }

        SubCategory savedSubCategory = mapper.dtoToEntity(subCategoryDto);
        savedSubCategory.setCategory(category);
        savedSubCategory = subCategoryRepository.save(savedSubCategory);
        return mapper.entityToResponseDto(savedSubCategory);
    }

    @Override
    public SubCategoryResponseDto getById(UUID id) {
        SubCategory subCategory = subCategoryRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("SubCategory with id " + id +
                " was not found"));
        return mapper.entityToResponseDto(subCategory);
    }

    @Override
    public SubCategoryResponseDto update(UUID id, SubCategoryBaseDto subCategoryDto){
        SubCategory subCategory = subCategoryRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("SubCategory with id " + id +
                " was not found"));
        if (subCategory.isDeleted()) {
            throw new DeletedException("Cannot update deleted SubCategory with id" + id
                    );
        }
        mapper.update(subCategoryDto, subCategory);
        subCategory = subCategoryRepository.save(subCategory);
        return mapper.entityToResponseDto(subCategory);
    }

    @Override
    public void toArchive(UUID id) {
        boolean exists = subCategoryRepository.existsById(id);
            if (exists){
                subCategoryRepository.deleteById(id);
            } else {
                throw new NotFoundException("SubCategory with id" + id + " was not found");
        }
    }
}
