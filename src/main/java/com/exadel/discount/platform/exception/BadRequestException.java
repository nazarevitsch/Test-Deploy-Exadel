package com.exadel.discount.platform.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class BadRequestException extends RuntimeException {
    private final String message;
    private final UUID id;
    private final Class<?> type;
    private final String invalidFieldName;

    public String getErrorCode() {
        return type.getSimpleName() + "_bad_request";
    }

    public String getEntityType() {
        return type.getSimpleName();
    }
}
