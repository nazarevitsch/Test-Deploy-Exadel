package com.exadel.discount.platform.model.dto;


import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class VendorDto {

    @NotBlank
    @Size(min = 2, max = 50)
    private String name;
    @Size(min = 50, max = 200)
    private String description;
    @Email
    private String email;
    @NotBlank
    private String image;
}
