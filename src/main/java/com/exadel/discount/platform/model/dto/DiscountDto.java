package com.exadel.discount.platform.model.dto;

import com.exadel.discount.platform.model.Vendor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;
import java.util.List;

@Data
public class DiscountDto {

    @NotBlank
    @Size(min = 2, max = 50)
    private String name;
    private List<TagDto> tags;
    @NotEmpty
    private List<VendorLocationDto> locations;
    private Vendor vendor;
    @Size(min = 50, max = 200)
    private String fullDescription;
    private boolean isActive;
    private boolean isOnline;
    @Size(min = 2, max = 80)
    private String imageLink;
    private ZonedDateTime startDate;
    private ZonedDateTime endDate;
    private boolean deleted;
}
