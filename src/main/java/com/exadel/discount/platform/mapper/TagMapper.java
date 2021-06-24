package com.exadel.discount.platform.mapper;

import com.exadel.discount.platform.model.Tag;
import com.exadel.discount.platform.model.dto.TagDto;
import com.exadel.discount.platform.model.dto.TagResponseDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TagMapper {
    final private ModelMapper modelMapper;

    public TagMapper() {
        modelMapper = new ModelMapper();
    }

    public TagDto entityToDto(Tag tag) {
        return modelMapper.map(tag, TagDto.class);
    }

    public TagResponseDto entityToResponseDto(Tag tag) {
        return modelMapper.map(tag, TagResponseDto.class);
    }

    public Tag dtoToEntity(TagDto tagDto) {
        return modelMapper.map(tagDto, Tag.class);
    }

    public void update(TagDto tagDto, Tag tag) {
        modelMapper.map(tagDto, tag);
    }

    public <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
        return source
                .stream()
                .map(element -> modelMapper.map(element, targetClass))
                .collect(Collectors.toList());
    }
}
