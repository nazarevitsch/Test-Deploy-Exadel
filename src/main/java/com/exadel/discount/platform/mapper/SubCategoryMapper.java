package com.exadel.discount.platform.mapper;

import com.exadel.discount.platform.model.SubCategory;
import com.exadel.discount.platform.model.dto.SubCategoryResponseDto;
import com.exadel.discount.platform.model.dto.update.SubCategoryBaseDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SubCategoryMapper {
    final private ModelMapper modelMapper;

    public SubCategoryMapper() {
        modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

    }

    public SubCategoryBaseDto entityToDto(SubCategory subCategory) {
        return modelMapper.map(subCategory, SubCategoryBaseDto.class);
    }

    public SubCategoryResponseDto entityToResponseDto(SubCategory subCategory) {
        SubCategoryResponseDto responseDto = modelMapper.map(subCategory, SubCategoryResponseDto.class);
        responseDto.setCategoryId(subCategory.getCategory().getId());
        return responseDto;
    }

    public SubCategory dtoToEntity(SubCategoryBaseDto subCategoryBaseDto) {
        return modelMapper.map(subCategoryBaseDto, SubCategory.class);
    }

    public void update(SubCategoryBaseDto subCategoryBaseDto, SubCategory subCategory) {
        modelMapper.map(subCategoryBaseDto, subCategory);
    }

    public List<SubCategoryResponseDto> mapList(List<SubCategory> categories) {
        return categories.stream().map(this::entityToResponseDto).collect(Collectors.toList());
    }
}
