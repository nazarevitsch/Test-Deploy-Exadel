package com.exadel.discount.platform.mapper;

import com.exadel.discount.platform.model.Category;
import com.exadel.discount.platform.model.SubCategory;
import com.exadel.discount.platform.model.dto.CategoryDto;
import com.exadel.discount.platform.model.dto.CategoryResponseDto;
import com.exadel.discount.platform.model.dto.CategoryStatisticResponseDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class CategoryMapper {
    private final ModelMapper modelMapper;

    public CategoryMapper() {
        modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    public CategoryDto entityToDto(Category category) {
        return modelMapper.map(category, CategoryDto.class);
    }

    public CategoryResponseDto entityToResponseDto(Category category) {
        CategoryResponseDto categoryResponseDto = modelMapper.map(category, CategoryResponseDto.class);
        List<UUID> subCategoryIds = category.getSubCategories().stream().map(SubCategory::getId).collect(Collectors.toList());
        categoryResponseDto.setSubCategoryIds(subCategoryIds);
        return categoryResponseDto;
    }

    public CategoryStatisticResponseDto entityToResponseStatisticDto(Category category) {
        return modelMapper.map(category, CategoryStatisticResponseDto.class);
    }

    public void update(CategoryDto categoryDto, Category category) {
        modelMapper.map(categoryDto, category);
    }

    public Category dtoToEntity(CategoryDto categoryDto) {
        return modelMapper.map(categoryDto, Category.class);
    }

    public List<CategoryResponseDto> mapList(List<Category> categories) {
        return categories.stream().map(this::entityToResponseDto).collect(Collectors.toList());
    }

    public List<CategoryStatisticResponseDto> map(List<Category> categories) {
        return categories.stream().map(this::entityToResponseStatisticDto).collect(Collectors.toList());
    }
}
