package com.exadel.discount.platform.service;

import com.exadel.discount.platform.exception.NotFoundException;
import com.exadel.discount.platform.mapper.TagMapper;
import com.exadel.discount.platform.model.Tag;
import com.exadel.discount.platform.model.dto.TagDto;
import com.exadel.discount.platform.model.dto.TagResponseDto;
import com.exadel.discount.platform.repository.TagRepository;
import com.exadel.discount.platform.service.interfaces.TagCrudService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class TagCrudServiceImpl implements TagCrudService {

    private final TagRepository tagRepository;
    private final TagMapper mapper;

    @Override
    public List<TagResponseDto> getAll() {
        return mapper.mapList(tagRepository.findAll(), TagResponseDto.class);
    }

    @Override
    public TagResponseDto save(TagDto tagDto) {
        Tag savedTag = tagRepository.save(mapper.dtoToEntity(tagDto));
        return mapper.entityToResponseDto(savedTag);
    }

    @Override
    public TagResponseDto getById(UUID id) {
        Tag tag = tagRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Tag with id " + id +
                " was not found"));
        return mapper.entityToResponseDto(tag);
    }

    @Override
    public TagResponseDto update(UUID id, TagDto tagDto){
        Tag tag = tagRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Tag with id " + id +
                " was not found"));
        mapper.update(tagDto, tag);
        tag = tagRepository.save(tag);
        return mapper.entityToResponseDto(tag);
    }

    @Override
    public void toArchive(UUID id) {
        boolean exists = tagRepository.existsById(id);
            if (exists){
                tagRepository.deleteById(id);
            } else {
                throw new NotFoundException("tag with id" + id + " was not found");
        }
    }
}
