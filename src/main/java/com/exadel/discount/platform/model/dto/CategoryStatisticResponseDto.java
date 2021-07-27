package com.exadel.discount.platform.model.dto;

import lombok.Data;

@Data
public class CategoryStatisticResponseDto extends  CategoryResponseDto {

    private int usageCount;
}
