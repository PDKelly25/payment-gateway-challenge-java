package com.checkout.payment.gateway.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Model representing the bank simulator's response to a payment request.
 * <p>
 * This class encapsulates the outcome of a payment authorization attempt
 * returned by the bank. It includes whether the payment was authorized
 * and the associated authorization code. The fields are mapped to JSON
 * properties to ensure correct serialization and deserialization when
 * communicating with external systems.
 * </p>
 */
public class BankPostPaymentResponse {

  /**
   * Indicates whether the payment request was authorized by the bank.
   */
  private boolean authorized;

  /**
   * The authorization code returned by the bank.
   * <p>
   * This field is serialized as {@code "authorization_code"} in JSON.
   * </p>
   */
  @JsonProperty("authorization_code")
  private String authorizationCode;

  /**
   * Sets whether the payment was authorized.
   *
   * @param authorized {@code true} if the payment was authorized, {@code false} otherwise
   */
  public void setAuthorized(boolean authorized) {
    this.authorized = authorized;
  }

  /**
   * Returns whether the payment was authorized.
   *
   * @return {@code true} if the payment was authorized, {@code false} otherwise
   */
  public boolean isAuthorized() {
    return authorized;
  }

  /**
   * Sets the authorization code returned by the bank.
   *
   * @param authorizationCode the authorization code string
   */
  public void setAuthorizationCode(String authorizationCode) {
    this.authorizationCode = authorizationCode;
  }

  /**
   * Returns the authorization code returned by the bank.
   *
   * @return the authorization code string
   */
  public String getAuthorizationCode() {
    return authorizationCode;
  }

  /**
   * Returns a string representation of the bank response.
   *
   * @return a string containing the authorization status and code
   */
  @Override
  public String toString() {
    return "GetPaymentResponse{" +
        "authorized=" + authorized +
        ", status=" + authorizationCode +
        '}';
  }
}
