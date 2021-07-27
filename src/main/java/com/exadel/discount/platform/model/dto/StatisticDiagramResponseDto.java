package com.exadel.discount.platform.model.dto;

import lombok.Data;
import java.util.List;

@Data
public class StatisticDiagramResponseDto {

    private List<DiscountDtoResponse> bestDiscounts;

    private List<VendorStatisticResponseDto> bestVendors;

    private List<CategoryStatisticResponseDto> bestCategories;
}
