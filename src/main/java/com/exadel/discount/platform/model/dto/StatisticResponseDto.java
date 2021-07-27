package com.exadel.discount.platform.model.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class StatisticResponseDto {

    private UUID id;

    private String name;

    private String vendorName;

    private String usageDate;

    private String categoryName;

    private String userEmail;
}
