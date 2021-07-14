package com.exadel.discount.platform.model.dto;

import com.exadel.discount.platform.model.dto.update.SubCategoryBaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class SubCategoryResponseDto extends SubCategoryBaseDto {
    @NotNull
    private UUID id;
    @NotNull
    private UUID categoryId;
    private boolean deleted;
}
