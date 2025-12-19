package com.checkout.payment.gateway.exception;

import org.springframework.http.HttpStatusCode;

/**
 * Custom runtime exception representing an error response from the bank simulator.
 * <p>
 * This exception is thrown when the bank returns an error (e.g. HTTP 5xx),
 * allowing the payment gateway to normalize upstream errors into a
 * domain-specific type. It encapsulates both a descriptive message and
 * the associated {@link HttpStatusCode} returned by the bank.
 * </p>
 */
public class BankResponseException extends RuntimeException {
  private HttpStatusCode statusCode;

  /**
   * Constructs a new {@code BankResponseException} with the specified message and status code.
   *
   * @param message    a human-readable description of the error
   * @param statusCode the HTTP status code returned by the bank
   */
  public BankResponseException(String message, HttpStatusCode statusCode){
    super(message);
    this.statusCode = statusCode;
  }

  /**
   * Returns the HTTP status code associated with the bank's error response.
   *
   * @return the {@link HttpStatusCode} returned by the bank
   */
  public HttpStatusCode getStatusCode() {
    return statusCode;
  }

  /**
   * Updates the HTTP status code associated with this exception.
   * <p>
   * This setter can be modified if needed, though should be
   * expected to remain immutable once the exception is constructed.
   * </p>
   *
   * @param statusCode the new {@link HttpStatusCode} to associate with this exception
   */
  public void setStatusCode(HttpStatusCode statusCode) {
    this.statusCode = statusCode;
  }
}
