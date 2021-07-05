package com.exadel.discount.platform.web;

import com.exadel.discount.platform.exception.NotFoundException;
import com.exadel.discount.platform.model.dto.SubCategoryDto;
import com.exadel.discount.platform.model.dto.SubCategoryResponseDto;
import com.exadel.discount.platform.model.dto.update.SubCategoryBaseDto;
import com.exadel.discount.platform.service.interfaces.SubCategoryService;
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
@RequestMapping("/sub_category")
public class SubCategoryController {

    private final SubCategoryService subCategoryService;

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('ADMINISTRATOR','USER')")
    public ResponseEntity<List<SubCategoryResponseDto>> getAllSubCategories() {
        return new ResponseEntity<>(subCategoryService.getAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    public ResponseEntity<SubCategoryResponseDto> getSubCategoryById(@PathVariable UUID id) {
        return new ResponseEntity<>(subCategoryService.getById(id), HttpStatus.OK);
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    public ResponseEntity<SubCategoryResponseDto> createSubCategory(@RequestBody SubCategoryDto subCategoryDto) {
        SubCategoryResponseDto savedSubCategory = subCategoryService.save(subCategoryDto);
        return new ResponseEntity<>(savedSubCategory, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    public ResponseEntity<SubCategoryResponseDto> updateSubCategory(@PathVariable UUID id,
                                                                    @RequestBody SubCategoryBaseDto subCategoryDto) {
        SubCategoryResponseDto updateSubCategory = subCategoryService.update(id, subCategoryDto);
        return new ResponseEntity<>(updateSubCategory, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}",
        produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    public ResponseEntity<SubCategoryDto> sendSubCategoryToArchive(@PathVariable UUID id) throws NotFoundException {
        subCategoryService.toArchive(id);
        SubCategoryResponseDto archivedSubCategory = subCategoryService.getById(id);
        return new ResponseEntity<>(archivedSubCategory, HttpStatus.OK);
    }

}
