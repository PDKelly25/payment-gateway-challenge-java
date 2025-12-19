package com.checkout.payment.gateway.model;

import com.checkout.payment.gateway.enums.PaymentStatus;
import java.util.UUID;

/**
 * Model representing the response returned when retrieving a payment event.
 * <p>
 * This class encapsulates the details of a processed payment, including
 * its unique identifier, status, masked card details, expiry information,
 * currency, and amount. It is typically used in GET endpoints to provide
 * clients with the current state of a payment transaction.
 * </p>
 */
public class GetPaymentResponse {
  /**
   * Unique identifier for the payment event.
   */
  private UUID id;

  /**
   * The current status of the payment (e.g. AUTHORIZED, DECLINED, REJECTED).
   */
  private PaymentStatus status;

  /**
   * The last four digits of the card number used for the payment.
   */
  private int cardNumberLastFour;

  /**
   * The expiry month of the card used for the payment.
   */
  private int expiryMonth;

  /**
   * The expiry year of the card used for the payment.
   */
  private int expiryYear;

  /**
   * The currency in which the payment was processed.
   */
  private String currency;

  /**
   * The amount of the payment transaction.
   */
  private int amount;

  /**
   * Returns the unique identifier of the payment event.
   *
   * @return the payment event ID
   */
  public UUID getId() {
    return id;
  }

  /**
   * Sets the unique identifier of the payment event.
   *
   * @param id the payment event ID
   */
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   * Returns the current status of the payment.
   *
   * @return the payment status
   */
  public PaymentStatus getStatus() {
    return status;
  }

  /**
   * Sets the current status of the payment.
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
  public int getCardNumberLastFour() {
    return cardNumberLastFour;
  }

  /**
   * Sets the last four digits of the card number.
   *
   * @param cardNumberLastFour the last four digits of the card number
   */
  public void setCardNumberLastFour(int cardNumberLastFour) {
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
   * @return the currency string
   */
  public String getCurrency() {
    return currency;
  }

  /**
   * Sets the currency of the payment.
   *
   * @param currency the currency string
   */
  public void setCurrency(String currency) {
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
