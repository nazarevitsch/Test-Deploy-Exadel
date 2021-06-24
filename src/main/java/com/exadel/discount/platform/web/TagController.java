package com.exadel.discount.platform.web;

import com.exadel.discount.platform.exception.NotFoundException;
import com.exadel.discount.platform.model.dto.TagDto;
import com.exadel.discount.platform.model.dto.TagResponseDto;
import com.exadel.discount.platform.service.TagCrudServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tag")
public class TagController {

    private final TagCrudServiceImpl tagCrudService;

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ADMINISTRATOR','USER')")
    public ResponseEntity<List<TagResponseDto>> getAll() {
        return new ResponseEntity<>(tagCrudService.getAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<TagResponseDto> getTagById(@PathVariable UUID id) {
        return new ResponseEntity<>(tagCrudService.getById(id), HttpStatus.OK);
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<TagResponseDto> createTag(@RequestBody TagDto tagDto) {
        TagResponseDto tagDtoCreated = tagCrudService.save(tagDto);
        return new ResponseEntity<>(tagDtoCreated, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<TagResponseDto> updateTag(@PathVariable UUID id,
                                                    @RequestBody TagDto tagDto) {
        TagResponseDto tagResponseDtoUpdate = tagCrudService.update(id, tagDto);
        return new ResponseEntity<>(tagResponseDtoUpdate, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}",
        produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<TagDto> sendToArchive(@PathVariable UUID id) throws NotFoundException {
        tagCrudService.toArchive(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
