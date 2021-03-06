package com.exadel.discount.platform.model.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class CategoryDto {

    @NotBlank
    @Size(min = 3, max = 50)
    private String name;
    private List<SubCategoryDto> subCategoryDtos;
}
