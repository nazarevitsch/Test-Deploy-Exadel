package com.exadel.discount.platform.model.dto;

import com.exadel.discount.platform.domain.Discount;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class DiscountDto {

    @NotBlank
    @Size(min = 2, max = 50)
    private String name;

    @NotNull
    private List<UUID> locationIds;

    @NotNull
    private UUID categoryId;

    @NotNull
    private List<UUID> subCategoryIds;

    @NotNull
    private UUID vendorId;

    @Size(min = 50, max = 2000)
    private String fullDescription;

    private int percentage;

    private boolean isOnline;

    private String imageLink;

    private ZonedDateTime startDate;

    private ZonedDateTime endDate;
}
