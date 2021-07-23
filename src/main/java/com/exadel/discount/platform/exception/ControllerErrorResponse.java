package com.exadel.discount.platform.exception;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ControllerErrorResponse {
    private String errorCode;
    private String message;
    private UUID id;
    private String entityType;
    private String invalidFieldName;
}
