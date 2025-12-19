package com.checkout.payment.gateway.model;

/**
 * Model representing a standardized error response returned by the API.
 * <p>
 * This class encapsulates error details in a simple structure that can be
 * serialized into JSON and sent back to clients whenever an exception occurs.
 * It ensures consistency in how error messages are communicated across
 * different parts of the application.
 * </p>
 */
public class ErrorResponse {
  /**
   * The human-readable error message describing the cause of failure.
   */
  private final String message;

  /**
   * Constructs a new {@code ErrorResponse} with the specified message.
   *
   * @param message the descriptive error message to return to the client
   */
  public ErrorResponse(String message) {
    this.message = message;
  }

  /**
   * Returns the error message associated with this response.
   *
   * @return the error message string
   */
  public String getMessage() {
    return message;
  }

  /**
   * Returns a string representation of the error response.
   *
   * @return a string containing the error message
   */
  @Override
  public String toString() {
    return "ErrorResponse{" +
        "message='" + message + '\'' +
        '}';
  }
}
