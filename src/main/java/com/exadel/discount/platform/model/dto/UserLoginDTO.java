package com.exadel.discount.platform.model.dto;

import lombok.Data;
import lombok.ToString;

@Data
public class UserLoginDTO {

    private String email;

    @ToString.Exclude
    private String password;
}
