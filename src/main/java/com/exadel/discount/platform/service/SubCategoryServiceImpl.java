package com.exadel.discount.platform.service;



import com.exadel.discount.platform.exception.DeletedException;
import com.exadel.discount.platform.exception.NotFoundException;
import com.exadel.discount.platform.mapper.SubCategoryMapper;
import com.exadel.discount.platform.model.SubCategory;
import com.exadel.discount.platform.model.dto.SubCategoryDto;
import com.exadel.discount.platform.model.dto.SubCategoryResponseDto;
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

    @Override
    public List<SubCategoryResponseDto> getAll() {
        return mapper.mapList(subCategoryRepository.findAll(), SubCategoryResponseDto.class);
    }

    @Override
    public SubCategoryResponseDto save(SubCategoryDto subCategoryDto) {
        SubCategory savedSubCategory = subCategoryRepository.save(mapper.dtoToEntity(subCategoryDto));
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
    public SubCategoryResponseDto update(UUID id, SubCategoryDto subCategoryDto){
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
