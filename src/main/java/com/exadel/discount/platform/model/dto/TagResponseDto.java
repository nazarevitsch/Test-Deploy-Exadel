package com.exadel.discount.platform.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class TagResponseDto extends TagDto {
    @NotNull
    private UUID id;
}
