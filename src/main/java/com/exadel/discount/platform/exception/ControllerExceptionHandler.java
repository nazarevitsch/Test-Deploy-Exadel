package com.exadel.discount.platform.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({NotFoundException.class})
    protected ResponseEntity<ControllerErrorResponse> handleNotFoundException(
            NotFoundException ex) {
        ControllerErrorResponse controllerResponse = ControllerErrorResponse.builder()
                .entityType(ex.getEntityType())
                .message(ex.getMessage())
                .id(ex.getId())
                .errorCode(ex.getErrorCode())
                .build();
        return new ResponseEntity<>(controllerResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({DeletedException.class})
    protected ResponseEntity<ControllerErrorResponse> handleDeletedException(
            DeletedException ex) {
        ControllerErrorResponse controllerResponse = ControllerErrorResponse.builder()
                .entityType(ex.getEntityType())
                .message(ex.getMessage())
                .id(ex.getId())
                .errorCode(ex.getErrorCode())
                .build();
        return new ResponseEntity<>(controllerResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({BadRequestException.class})
    protected ResponseEntity<ControllerErrorResponse> handleBadRequestException(
            BadRequestException ex) {
        ControllerErrorResponse controllerResponse = ControllerErrorResponse.builder()
                .entityType(ex.getEntityType())
                .message(ex.getMessage())
                .id(ex.getId())
                .errorCode(ex.getErrorCode())
                .invalidFieldName(ex.getInvalidFieldName())
                .build();
        return new ResponseEntity<>(controllerResponse, HttpStatus.BAD_REQUEST);
    }
}
