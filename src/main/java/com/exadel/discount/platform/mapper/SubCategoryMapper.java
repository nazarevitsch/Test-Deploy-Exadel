package com.exadel.discount.platform.mapper;

import com.exadel.discount.platform.model.SubCategory;
import com.exadel.discount.platform.model.dto.SubCategoryDto;
import com.exadel.discount.platform.model.dto.SubCategoryResponseDto;
import com.exadel.discount.platform.model.dto.update.SubCategoryBaseDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class SubCategoryMapper {
    final private ModelMapper modelMapper;

    public SubCategoryMapper() {
        modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

    }

    public SubCategoryDto entityToDto(SubCategory subCategory) {
        return modelMapper.map(subCategory, SubCategoryDto.class);
    }

    public SubCategoryResponseDto entityToResponseDto(SubCategory subCategory) {
        SubCategoryResponseDto responseDto = modelMapper.map(subCategory, SubCategoryResponseDto.class);
        responseDto.setCategoryId(subCategory.getCategory().getId());
        return responseDto;
    }

    public SubCategory dtoToEntity(SubCategoryDto subCategoryDto) {
        return modelMapper.map(subCategoryDto, SubCategory.class);
    }

    public void update(SubCategoryBaseDto subCategoryDto, SubCategory subCategory) {
        modelMapper.map(subCategoryDto, subCategory);
    }

    public List<SubCategoryResponseDto> mapList(List<SubCategory> categories){
        return categories.stream().map(this::entityToResponseDto).collect(Collectors.toList());
    }
}
