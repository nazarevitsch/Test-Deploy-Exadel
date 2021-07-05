package com.exadel.discount.platform.model.dto;

import com.exadel.discount.platform.model.dto.update.SubCategoryBaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class SubCategoryDto extends SubCategoryBaseDto {

    @NotNull
    private UUID categoryId;
    @ToString.Exclude
    private List<DiscountDto> discounts;
}
