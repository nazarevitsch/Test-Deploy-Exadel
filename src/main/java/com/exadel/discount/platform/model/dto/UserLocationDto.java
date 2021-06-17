package com.exadel.discount.platform.model.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class UserLocationDto {

    @NotBlank
    @Size(min = 3, max = 60)
    private String country;
    @Size(min = 2, max = 50)
    private String city;
    @Size(min = 2, max = 50)
    private String addressLine;
    //private MyUserDetailsDto user;
    private boolean deleted;
}
