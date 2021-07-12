package com.exadel.discount.platform.model.dto;

import lombok.Data;

import javax.validation.constraints.*;
import java.util.List;
import java.util.UUID;

@Data
public class DiscountDto extends DiscountBaseDto {

    @NotNull
    private List<UUID> locationIds;

    @NotNull
    private List<UUID> subCategoryIds;
}
