package com.exadel.discount.platform.mapper;

import com.exadel.discount.platform.model.SubCategory;
import com.exadel.discount.platform.model.dto.SubCategoryDto;
import com.exadel.discount.platform.model.dto.SubCategoryResponseDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SubCategoryMapper {
    final private ModelMapper modelMapper;

    public SubCategoryMapper() {
        modelMapper = new ModelMapper();
    }

    public SubCategoryDto entityToDto(SubCategory subCategory) {
        return modelMapper.map(subCategory, SubCategoryDto.class);
    }

    public SubCategoryResponseDto entityToResponseDto(SubCategory subCategory) {
        return modelMapper.map(subCategory, SubCategoryResponseDto.class);
    }

    public SubCategory dtoToEntity(SubCategoryDto subCategoryDto) {
        return modelMapper.map(subCategoryDto, SubCategory.class);
    }

    public void update(SubCategoryDto subCategoryDto, SubCategory subCategory) {
        modelMapper.map(subCategoryDto, subCategory);
    }

    public <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
        return source
                .stream()
                .map(element -> modelMapper.map(element, targetClass))
                .collect(Collectors.toList());
    }
}
