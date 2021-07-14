package com.exadel.discount.platform.model.dto;

import com.exadel.discount.platform.model.dto.update.VendorLocationBaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class VendorLocationResponseDto extends VendorLocationBaseDto {
    @NotNull
    private UUID id;
    @NotNull
    private UUID vendorId;
    private boolean deleted;
}
