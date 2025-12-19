package com.checkout.payment.gateway.exception;

/**
 * Custom runtime exception representing failures during event processing.
 * <p>
 * This exception is thrown when the application encounters an error while
 * handling or retrieving a payment event. It provides a simple mechanism
 * for signaling event-related issues to higher layers of the application,
 * where they can be intercepted and translated into user-facing error
 * responses by a global exception handler.
 * </p>
 */
public class EventProcessingException extends RuntimeException{

  /**
   * Constructs a new {@code EventProcessingException} with the specified detail message.
   *
   * @param message a human-readable description of the event processing error
   */
  public EventProcessingException(String message) {
    super(message);
  }
}
