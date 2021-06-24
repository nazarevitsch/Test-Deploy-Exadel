package com.exadel.discount.platform.mapper;

import com.exadel.discount.platform.model.Tag;
import com.exadel.discount.platform.model.dto.TagDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import com.exadel.discount.platform.model.dto.TagResponseDto;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TagMapper {
    final private ModelMapper modelMapper;

    public TagMapper() {
        modelMapper = new ModelMapper();
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

    public List<TagDto> mapList(List<Tag> source, Class<TagDto> targetClass) {
        return source
                .stream()
                .map(element -> modelMapper.map(element, targetClass))
                .collect(Collectors.toList());
    }
}
