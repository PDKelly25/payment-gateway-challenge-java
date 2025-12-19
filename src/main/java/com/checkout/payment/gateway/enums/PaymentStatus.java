package com.checkout.payment.gateway.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enumeration representing the possible statuses of a payment transaction.
 * <p>
 * Each status corresponds to the outcome of a payment request as determined
 * by the gateway or external bank simulator. The enum values are serialized
 * into JSON using their human-readable names (e.g. "Authorized") rather than
 * the enum constant names.
 * </p>
 */
public enum PaymentStatus {
  /**
   * Indicates that the payment request was successfully authorized.
   */
  AUTHORIZED("Authorized"),

  /**
   * Indicates that the payment request was declined by the bank or gateway.
   */
  DECLINED("Declined"),

  /**
   * Indicates that the payment request was rejected due to validation or other errors.
   */
  REJECTED("Rejected");

  private final String name;

  /**
   * Constructs a {@code PaymentStatus} with the given display name.
   *
   * @param name the human-readable name of the status
   */
  PaymentStatus(String name) {
    this.name = name;
  }

  /**
   * Returns the human-readable name of the payment status.
   * <p>
   * This method is annotated with {@link JsonValue}, meaning that when
   * serialized to JSON, the enum will be represented by this string value
   * instead of the enum constant name.
   * </p>
   *
   * @return the display name of the payment status
   */
  @JsonValue
  public String getName() {
    return this.name;
  }
}
