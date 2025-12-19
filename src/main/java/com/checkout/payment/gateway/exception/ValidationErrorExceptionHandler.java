package com.checkout.payment.gateway.exception;

import com.checkout.payment.gateway.enums.PaymentStatus;
import com.checkout.payment.gateway.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Global exception handler for validation errors.
 * <p>
 * This class intercepts {@link MethodArgumentNotValidException} thrown when
 * request payloads fail validation constraints (e.g. invalid fields, missing values).
 * It aggregates field and global validation errors into a structured message
 * and returns a standardized {@link ErrorResponse} to the client.
 * </p>
 */
@ControllerAdvice
public class ValidationErrorExceptionHandler {

  /**
   * Handles {@link MethodArgumentNotValidException} thrown during request validation.
   * <p>
   * This method collects all field and global errors from the binding result,
   * formats them into a human-readable string, and wraps them in an
   * {@link ErrorResponse}. The response includes a {@link PaymentStatus#REJECTED}
   * indicator to signal that the payment request was rejected due to validation issues.
   * </p>
   *
   * @param ex the validation exception containing details of invalid fields and objects
   * @return a {@link ResponseEntity} containing the {@link ErrorResponse} and HTTP 400 status
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationExceptions(
      MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();

    ex.getBindingResult().getFieldErrors().forEach(error ->
        errors.put(error.getField(), error.getDefaultMessage()));

    ex.getBindingResult().getGlobalErrors().forEach(error ->
        errors.put(error.getObjectName(), error.getDefaultMessage()));

    String message_errors = errors.entrySet()
        .stream()
        .map(entry -> entry.getKey() + ": " + entry.getValue())
        .collect(Collectors.joining(",\n "));

    String message = String.format("Payment Status: %s \n %s", PaymentStatus.REJECTED,
        message_errors);
    return new ResponseEntity<>(new ErrorResponse(message), HttpStatus.BAD_REQUEST);
  }
}
