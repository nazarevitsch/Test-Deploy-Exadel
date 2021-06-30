package com.exadel.discount.platform.mapper;

import com.exadel.discount.platform.model.Category;
import com.exadel.discount.platform.model.dto.CategoryDto;
import com.exadel.discount.platform.model.dto.CategoryResponseDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoryMapper {
    private final ModelMapper modelMapper;

    public CategoryMapper() {
        modelMapper = new ModelMapper();
    }

    public CategoryDto entityToDto(Category category) {
        return modelMapper.map(category, CategoryDto.class);
    }

    public CategoryResponseDto entityToResponseDto(Category category) {
        return modelMapper.map(category, CategoryResponseDto.class);
    }

    public void update(CategoryDto categoryDto, Category category) {
        modelMapper.map(categoryDto, category);
    }

    public Category dtoToEntity(CategoryDto categoryDto) {
        return modelMapper.map(categoryDto, Category.class);
    }

    public <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
        return source
                .stream()
                .map(element -> modelMapper.map(element, targetClass))
                .collect(Collectors.toList());
    }
}
