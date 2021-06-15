package com.exadel.discount.platform.service.dto;

import lombok.Data;

@Data
public class UserLoginDTO {
    private String email;
    private String password;

    @Override
    public String toString() {
        return "UserLoginRequest{" +
                "email='" + email + '\'' +
                '}';
    }
}
