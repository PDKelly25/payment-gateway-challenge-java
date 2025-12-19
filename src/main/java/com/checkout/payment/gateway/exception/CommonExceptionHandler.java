package com.checkout.payment.gateway.exception;

import com.checkout.payment.gateway.model.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Global exception handler for common application errors.
 * <p>
 * This class is annotated with {@link ControllerAdvice}, allowing it to intercept
 * exceptions thrown during request processing across all controllers. It provides
 * a centralized mechanism for handling errors that are not specific to bank responses,
 * such as event processing failures.
 * </p>
 */
@ControllerAdvice
public class CommonExceptionHandler {

  private static final Logger LOG = LoggerFactory.getLogger(CommonExceptionHandler.class);

  /**
   * Handles {@link EventProcessingException} thrown by the application.
   * <p>
   * This method logs the exception details and returns a standardized
   * {@link ErrorResponse} with a fixed message. The HTTP status code
   * is set to {@link HttpStatus#NOT_FOUND}, indicating that the requested
   * resource or event could not be located or processed.
   * </p>
   *
   * @param ex the {@link EventProcessingException} thrown during request processing
   * @return a {@link ResponseEntity} containing the {@link ErrorResponse} and HTTP 404 status
   */
  @ExceptionHandler(EventProcessingException.class)
  public ResponseEntity<ErrorResponse> handleException(EventProcessingException ex) {
    LOG.error("Exception happened", ex);
    return new ResponseEntity<>(new ErrorResponse("Page not found"),
        HttpStatus.NOT_FOUND);
  }
}
