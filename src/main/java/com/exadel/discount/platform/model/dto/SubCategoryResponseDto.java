package com.exadel.discount.platform.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class SubCategoryResponseDto extends SubCategoryDto {
    @NotNull
    private UUID id;
    private boolean deleted;
}
