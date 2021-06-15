package com.exadel.discount.platform.domain;

import lombok.Data;

@Data
public class JWTResponse {

    private String token;
    public JWTResponse() {}

    public JWTResponse(String token) {
        this.token = token;
    }
}