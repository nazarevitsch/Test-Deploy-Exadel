package com.exadel.discount.platform.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class NotFoundException extends RuntimeException {
    private final String message;
    private final UUID id;
    private final Class<?> type;

    public String getErrorCode() {
        return type.getSimpleName() + "_not_found";
    }

    public String getEntityType() {
        return type.getSimpleName();
    }
}
