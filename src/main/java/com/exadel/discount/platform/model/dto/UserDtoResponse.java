package com.exadel.discount.platform.model.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class UserDtoResponse {

    private UUID id;

    private String email;
}
