package com.backend.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.backend.exception.GenericErrorResponse;
import com.backend.exception.OrderActionNotAllowedInStageException;
import com.backend.exception.ProductOutOfStockException;
import com.backend.exception.ResourceNotFoundException;
import com.backend.exception.ValidationErrorResponse;
import com.backend.exception.ValidationErrorResponse.ErrorDetail;
import com.backend.exception.ValidationException;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ValidationErrorResponse> handleConstraintViolationException(
            ConstraintViolationException ex) {
        List<ErrorDetail> errorDetails = new ArrayList<>();
        ValidationErrorResponse errorResponse = new ValidationErrorResponse();
        errorResponse.setErrors(errorDetails);

        ex.getConstraintViolations().forEach(cv -> {
            var errorDetail = new ErrorDetail();
            String path = cv.getPropertyPath().toString();
            String field = path.substring(path.indexOf('.') + 1);

            errorDetail.setField(field);
            errorDetail.setMessage(cv.getMessage());

            errorDetails.add(errorDetail);
        });

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationException(
            ValidationException ex) {
        ValidationErrorResponse errorResponse = new ValidationErrorResponse();
        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setField(ex.getFiled());
        errorDetail.setMessage(ex.getMessage());
        errorResponse.setErrors(List.of(errorDetail));

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ProductOutOfStockException.class)
    public ResponseEntity<ValidationErrorResponse> handleProductOutOfStockException(ProductOutOfStockException ex) {
        ValidationErrorResponse errorResponse = new ValidationErrorResponse();
        errorResponse.setStatus(HttpStatus.CONFLICT.value());
        errorResponse.setMessage(ex.getMessage());
        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setField("productCode");
        errorDetail.setMessage(ex.getProductCode());

        ErrorDetail errorDetail2 = new ErrorDetail();
        errorDetail2.setField("quantity");
        errorDetail2.setMessage(String.valueOf(ex.getQuantity()));

        errorResponse.setErrors(List.of(errorDetail, errorDetail2));

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(OrderActionNotAllowedInStageException.class)
    public ResponseEntity<GenericErrorResponse> handleOrderActionNotAllowedInStageException(
            OrderActionNotAllowedInStageException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                GenericErrorResponse.builder()
                        .status(HttpStatus.CONFLICT.value())
                        .error("Invalid Request for current Order Stage")
                        .message(ex.getMessage())
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<ErrorDetail> errorDetails = new ArrayList<>();
        ValidationErrorResponse errorResponse = new ValidationErrorResponse();
        errorResponse.setErrors(errorDetails);
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            var errorDetail = new ErrorDetail();
            errorDetail.setField(error.getField());
            errorDetail.setMessage(error.getDefaultMessage());

            errorDetails.add(errorDetail);
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<GenericErrorResponse> handleREsourseNotFoundException(ResourceNotFoundException ex) {
        GenericErrorResponse error = GenericErrorResponse
                .builder()
                .status(HttpStatus.NOT_FOUND.value())
                .error("Resource Not Found")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

}
