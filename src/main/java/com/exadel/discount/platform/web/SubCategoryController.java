package com.exadel.discount.platform.web;

import com.exadel.discount.platform.exception.NotFoundException;
import com.exadel.discount.platform.model.dto.SubCategoryResponseDto;
import com.exadel.discount.platform.model.dto.update.SubCategoryBaseDto;
import com.exadel.discount.platform.service.RoleCheckService;
import com.exadel.discount.platform.service.SubCategoryService;
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
@RequestMapping("/category/{categoryId}/sub_category")
public class SubCategoryController {

    private final SubCategoryService subCategoryService;
    private final RoleCheckService roleCheckService;

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('ADMINISTRATOR','USER')")
    public ResponseEntity<List<SubCategoryResponseDto>> getAllSubCategories(@PathVariable UUID categoryId,
                                                                            @RequestParam(value = "isDeleted", required = false, defaultValue = "false") boolean isDeleted
    ) {
        if (isDeleted && !roleCheckService.isAdmin()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(subCategoryService.getAllByCategoryId(categoryId, isDeleted), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('ADMINISTRATOR', 'USER')")
    public ResponseEntity<SubCategoryResponseDto> getSubCategoryById(@PathVariable UUID categoryId,
                                                                     @PathVariable UUID id) {
        SubCategoryResponseDto subCategoryBaseDto = subCategoryService.getByCategoryIdAndId(categoryId, id);
        if (subCategoryBaseDto.isDeleted() && !roleCheckService.isAdmin()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(subCategoryService.getByCategoryIdAndId(categoryId, id), HttpStatus.OK);
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    public ResponseEntity<SubCategoryResponseDto> createSubCategory(@PathVariable UUID categoryId,
                                                                    @RequestBody SubCategoryBaseDto subCategoryBaseDto) {
        SubCategoryResponseDto savedSubCategory = subCategoryService.save(categoryId, subCategoryBaseDto);
        return new ResponseEntity<>(savedSubCategory, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    public ResponseEntity<SubCategoryResponseDto> updateSubCategory(@PathVariable UUID categoryId,
                                                                    @PathVariable UUID id,
                                                                    @RequestBody SubCategoryBaseDto subCategoryBaseDto) {
        SubCategoryResponseDto updateSubCategory = subCategoryService.update(categoryId, id, subCategoryBaseDto);
        return new ResponseEntity<>(updateSubCategory, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    public ResponseEntity<SubCategoryBaseDto> sendSubCategoryToArchive(@PathVariable UUID categoryId,
                                                                       @PathVariable UUID id) throws NotFoundException {
        subCategoryService.toArchive(categoryId, id);
        SubCategoryResponseDto archivedSubCategory = subCategoryService.getByCategoryIdAndId(categoryId, id);
        return new ResponseEntity<>(archivedSubCategory, HttpStatus.OK);
    }
}
