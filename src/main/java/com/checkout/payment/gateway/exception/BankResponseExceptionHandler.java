package com.checkout.payment.gateway.exception;

import com.checkout.payment.gateway.model.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Global exception handler for {@link BankResponseException}.
 * <p>
 * This class is annotated with {@link ControllerAdvice}, allowing it to intercept
 * exceptions thrown during request processing across all controllers. It ensures
 * that upstream bank errors are consistently logged and translated into a
 * structured {@link ErrorResponse} for API clients.
 * </p>
 */
@ControllerAdvice
public class BankResponseExceptionHandler {

  private static final Logger LOG = LoggerFactory.getLogger(BankResponseExceptionHandler.class);

  /**
   * Handles {@link BankResponseException} thrown by the application.
   * <p>
   * This method logs the exception details and returns a standardized
   * {@link ErrorResponse} containing the error message. The HTTP status
   * code is derived from the {@link BankResponseException#getStatusCode()}
   * to ensure the response accurately reflects the upstream error.
   * </p>
   *
   * @param ex the {@link BankResponseException} thrown during request processing
   * @return a {@link ResponseEntity} containing the {@link ErrorResponse} and the associated HTTP status
   */
  @ExceptionHandler(BankResponseException.class)
  public ResponseEntity<ErrorResponse> handleException(BankResponseException ex) {
    LOG.error("Exception happened", ex);
    return new ResponseEntity<>(new ErrorResponse(ex.getMessage()), ex.getStatusCode());
  }
}
