package com.checkout.payment.gateway.model;

import com.checkout.payment.gateway.enums.ISOCurrencyCode;
import com.checkout.payment.gateway.enums.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;

/**
 * Model representing the response returned after processing a payment request.
 * <p>
 * This class encapsulates the normalized details of a payment transaction,
 * including its unique identifier, status, masked card details, expiry
 * information, currency, and amount. It is typically returned by POST
 * endpoints to provide clients with confirmation and outcome of a payment.
 * </p>
 */
public class PostPaymentResponse {
  /**
   * Unique identifier for the payment transaction.
   */
  private UUID id;

  /**
   * The status of the payment (e.g. AUTHORIZED, DECLINED, REJECTED).
   */
  private PaymentStatus status;

  /**
   * The last four digits of the card number used for the payment.
   * <p>
   * Serialized as {@code "card_number_last_four"} in JSON.
   * </p>
   */
  @JsonProperty("card_number_last_four")
  private String cardNumberLastFour;

  /**
   * The expiry month of the card used for the payment.
   * <p>
   * Serialized as {@code "expiry_month"} in JSON.
   * </p>
   */
  @JsonProperty("expiry_month")
  private int expiryMonth;

  /**
   * The expiry year of the card used for the payment.
   * <p>
   * Serialized as {@code "expiry_year"} in JSON.
   * </p>
   */
  @JsonProperty("expiry_year")
  private int expiryYear;

  /**
   * The currency in which the payment was processed.
   */
  private ISOCurrencyCode currency;

  /**
   * The amount of the payment transaction.
   */
  private int amount;

  /**
   * Returns the unique identifier of the payment transaction.
   *
   * @return the payment transaction ID
   */
  public UUID getId() {
    return id;
  }

  /**
   * Sets the unique identifier of the payment transaction.
   *
   * @param id the payment transaction ID
   */
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   * Returns the status of the payment.
   *
   * @return the payment status
   */
  public PaymentStatus getStatus() {
    return status;
  }

  /**
   * Sets the status of the payment.
   *
   * @param status the payment status
   */
  public void setStatus(PaymentStatus status) {
    this.status = status;
  }

  /**
   * Returns the last four digits of the card number.
   *
   * @return the last four digits of the card number
   */
  public String getCardNumberLastFour() {
    return cardNumberLastFour;
  }

  /**
   * Sets the last four digits of the card number.
   *
   * @param cardNumberLastFour the last four digits of the card number
   */
  public void setCardNumberLastFour(String cardNumberLastFour) {
    this.cardNumberLastFour = cardNumberLastFour;
  }

  /**
   * Returns the expiry month of the card.
   *
   * @return the expiry month
   */
  public int getExpiryMonth() {
    return expiryMonth;
  }

  /**
   * Sets the expiry month of the card.
   *
   * @param expiryMonth the expiry month
   */
  public void setExpiryMonth(int expiryMonth) {
    this.expiryMonth = expiryMonth;
  }

  /**
   * Returns the expiry year of the card.
   *
   * @return the expiry year
   */
  public int getExpiryYear() {
    return expiryYear;
  }

  /**
   * Sets the expiry year of the card.
   *
   * @param expiryYear the expiry year
   */
  public void setExpiryYear(int expiryYear) {
    this.expiryYear = expiryYear;
  }

  /**
   * Returns the currency of the payment.
   *
   * @return the currency
   */
  public ISOCurrencyCode getCurrency() {
    return currency;
  }

  /**
   * Sets the currency of the payment.
   *
   * @param currency the currency
   */
  public void setCurrency(ISOCurrencyCode currency) {
    this.currency = currency;
  }

  /**
   * Returns the amount of the payment.
   *
   * @return the payment amount
   */
  public int getAmount() {
    return amount;
  }

  /**
   * Sets the amount of the payment.
   *
   * @param amount the payment amount
   */
  public void setAmount(int amount) {
    this.amount = amount;
  }

  /**
   * Returns a string representation of the payment response.
   *
   * @return a string containing all payment details
   */
  @Override
  public String toString() {
    return "GetPaymentResponse{" +
        "id=" + id +
        ", status=" + status +
        ", cardNumberLastFour=" + cardNumberLastFour +
        ", expiryMonth=" + expiryMonth +
        ", expiryYear=" + expiryYear +
        ", currency='" + currency + '\'' +
        ", amount=" + amount +
        '}';
  }
}
