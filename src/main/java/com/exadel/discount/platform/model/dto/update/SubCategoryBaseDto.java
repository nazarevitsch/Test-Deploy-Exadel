package com.exadel.discount.platform.model.dto.update;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class SubCategoryBaseDto {
    @NotBlank
    @Size(min = 3, max = 20)
    private String name;
}
