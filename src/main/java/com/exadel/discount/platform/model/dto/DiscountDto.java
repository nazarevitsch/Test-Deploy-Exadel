package com.exadel.discount.platform.model.dto;

import com.exadel.discount.platform.domain.Discount;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class DiscountDto {

    @NotBlank
    @Size(min = 2, max = 50)
    private String name;

    private List<UUID> locations;

    private UUID categoryId;

    private List<UUID> subCategories;

    private UUID vendorId;

    @Size(min = 50, max = 2000)
    private String fullDescription;

    private boolean isOnline;

    private String imageLink;

    private ZonedDateTime startDate;

    private ZonedDateTime endDate;

    public Discount getDiscount(){
        Discount discount = new Discount();
        discount.setName(this.name);
        discount.setFullDescription(this.fullDescription);
        discount.setOnline(this.isOnline);
        discount.setEndDate(this.endDate);
        discount.setStartDate(this.startDate);
        discount.setImageLink(this.imageLink);
        discount.setVendorId(this.vendorId);
        discount.setCategoryId(this.categoryId);

        discount.setDeleted(false);
        return discount;
    }
}
