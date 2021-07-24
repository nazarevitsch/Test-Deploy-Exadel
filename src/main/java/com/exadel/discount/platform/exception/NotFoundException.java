package com.exadel.discount.platform.exception;

import lombok.Getter;

import java.util.UUID;

@Getter
public class NotFoundException extends RuntimeException {
    private final String message;
    private final UUID id;
    private final Class<?> type;

    public NotFoundException(String message, UUID id, Class<?> type) {
        super();
        this.message = message;
        this.id = id;
        this.type = type;
    }

    public String getErrorCode() {
        return type.getSimpleName() + "_not_found";
    }

    public String getEntityType() {
        return type.getSimpleName();
    }
}
