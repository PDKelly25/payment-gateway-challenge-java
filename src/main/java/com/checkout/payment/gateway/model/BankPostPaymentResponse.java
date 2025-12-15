package com.checkout.payment.gateway.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BankPostPaymentResponse {

  private boolean authorized;

  @JsonProperty("authorization_code")
  private String authorizationCode;

  public void setAuthorized(boolean authorized) {
    this.authorized = authorized;
  }

  public boolean isAuthorized() {
    return authorized;
  }

  public void setAuthorizationCode(String authorizationCode) {
    this.authorizationCode = authorizationCode;
  }

  public String getAuthorizationCode() {
    return authorizationCode;
  }

  @Override
  public String toString() {
    return "GetPaymentResponse{" +
        "authorized=" + authorized +
        ", status=" + authorizationCode +
        '}';
  }
}
