package com.example.accounting_service.exceptions.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.accounting_service.exceptions.InvalidHolderTypeException;
import com.example.accounting_service.exceptions.ResourceNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(ResourceNotFoundException.class)
        public ResponseEntity<ApiErrorResponse> handleResourceNotFound(
                ResourceNotFoundException ex,
                HttpServletRequest request
        ) {
                ApiErrorResponse error = new ApiErrorResponse(
                        HttpStatus.NOT_FOUND.value(),
                 "Resource not found",
                        ex.getMessage(),
                        request.getRequestURI()
                );

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }

        @ExceptionHandler(InvalidHolderTypeException.class)
        public ResponseEntity<ApiErrorResponse> handleInvalidHolderType(
                InvalidHolderTypeException ex,
                HttpServletRequest request
        ) {
                ApiErrorResponse error = new ApiErrorResponse(
                        HttpStatus.BAD_REQUEST.value(),
                        "Invalid holder type",
                        ex.getMessage(),
                        request.getRequestURI()
                );

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ApiErrorResponse> handleValidation(
                MethodArgumentNotValidException ex,
                HttpServletRequest request
        ) {
                String message = ex.getBindingResult()
                        .getFieldErrors()
                        .stream()
                        .map(error -> error.getField() + ": " + error.getDefaultMessage())
                        .findFirst()
                        .orElse("Validation error");

                ApiErrorResponse error = new ApiErrorResponse(
                        HttpStatus.BAD_REQUEST.value(),
                        "Validation error",
                        message,
                        request.getRequestURI()
                );

                return ResponseEntity.badRequest().body(error);
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ApiErrorResponse> handleGenericException(
                Exception ex,
                HttpServletRequest request
        ) {
                ApiErrorResponse error = new ApiErrorResponse(
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "Internal server error",
                        "Unexpected error occurred",
                        request.getRequestURI()
                );

                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
}
