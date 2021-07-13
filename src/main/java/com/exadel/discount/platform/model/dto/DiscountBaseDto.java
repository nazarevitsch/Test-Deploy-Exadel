package com.exadel.discount.platform.model.dto;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.ZonedDateTime;
import java.util.UUID;

@Data
public class DiscountBaseDto {

    @NotBlank
    @Size(min = 2, max = 50)
    private String name;

    @NotNull
    private UUID categoryId;

    @NotNull
    private UUID vendorId;

    @Size(min = 50, max = 2000)
    private String fullDescription;

    @Min(1)
    @Max(99)
    private int percentage;

    private boolean isOnline;

    private String imageLink;

    private ZonedDateTime startDate;

    private ZonedDateTime endDate;
}
