package com.checkout.payment.gateway.model;

import com.checkout.payment.gateway.enums.ISOCurrencyCode;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.YearMonth;

/**
 * Model representing a payment request submitted by a merchant.
 * <p>
 * This class encapsulates the details required to process a payment,
 * including card information, expiry date, currency, amount, and CVV.
 * It is annotated with validation constraints to ensure that incoming
 * requests are well-formed and secure before being processed by the gateway.
 * </p>
 */
public class PostPaymentRequest implements Serializable {

  /**
   * The full card number used for the payment.
   * <p>
   * Must be between 14 and 19 digits long and contain only numeric characters.
   * Serialized as {@code "card_number"} in JSON.
   * </p>
   */
  @JsonProperty("card_number")
  @NotNull(message="Card Number is required")
  @Size(min = 14, max = 19, message = "Card Number must be between 14 - 19 characters long")
  @Pattern(regexp = "\\d+", message = "Card Number must only contain numeric characters")
  private String cardNumber;

  /**
   * The expiry month of the card.
   * <p>
   * Must be between 1 and 12. Serialized as {@code "expiry_month"} in JSON.
   * </p>
   */
  @JsonProperty("expiry_month")
  @NotNull(message="Expiry Month is required")
  @Min(value = 1, message = "Expiry Month should (inclusively) be between 1 - 12")
  @Max(value = 12, message = "Expiry Month should (inclusively) be between 1 - 12")
  private int expiryMonth;

  /**
   * The expiry year of the card.
   * <p>
   * Serialized as {@code "expiry_year"} in JSON.
   * </p>
   */
  @JsonProperty("expiry_year")
  @NotNull(message="Expiry Year is required")
  private int expiryYear;


  /**
   * The currency in which the payment is processed.
   * <p>
   * Must be a valid ISO currency code defined in {@link ISOCurrencyCode}.
   * </p>
   */
  private ISOCurrencyCode currency;

  /**
   * The amount of the payment transaction.
   */
  private int amount;

  /**
   * The card CVV (Card Verification Value).
   * <p>
   * Must be 3–4 digits long and contain only numeric characters.
   * </p>
   */
  @NotNull(message="CVV is required")
  @Size(min = 3, max = 4, message = "CVV must be 3-4 characters long")
  @Pattern(regexp = "\\d+", message = "CVV must only contain numeric characters")
  private String cvv;

  public String getCardNumber() {
    return cardNumber;
  }

  public void setCardNumber(String cardNumber) {
    this.cardNumber = cardNumber;
  }

  public int getExpiryMonth() {
    return expiryMonth;
  }

  public void setExpiryMonth(int expiryMonth) {
    this.expiryMonth = expiryMonth;
  }

  public int getExpiryYear() {
    return expiryYear;
  }

  public void setExpiryYear(int expiryYear) {
    this.expiryYear = expiryYear;
  }

  /**
   * Validates that the expiry date is in the future.
   * <p>
   * This method ensures that the provided month and year represent a valid
   * future date. It guards against invalid values and rejects expired cards.
   * </p>
   *
   * @return {@code true} if the expiry date is valid and in the future, {@code false} otherwise
   */
  @AssertTrue(message = "Expiry Date must be in the future")
  public boolean isExpiryDateValid() {
    // Guard against invalid month/year values
    if (expiryMonth < 1 || expiryMonth > 12 || expiryYear <= 0) {
      return false;
    }
    LocalDate expiryDate = YearMonth.of(expiryYear, expiryMonth).atEndOfMonth();
    return expiryDate.isAfter(LocalDate.now());
  }

  public ISOCurrencyCode getCurrency() {
    return currency;
  }

  public void setCurrency(ISOCurrencyCode currency) {
    this.currency = currency;
  }

  public int getAmount() {
    return amount;
  }

  public void setAmount(int amount) {
    this.amount = amount;
  }

  public String getCvv() {
    return cvv;
  }

  public void setCvv(String cvv) {
    this.cvv = cvv;
  }

  /**
   * Returns the expiry date as a formatted string.
   * <p>
   * Serialized as {@code "expiry_date"} in JSON.
   * </p>
   *
   * @return the expiry date in {@code MM/YYYY} format
   */
  @JsonProperty("expiry_date")
  public String getExpiryDate() {
    return String.format("%d/%d", expiryMonth, expiryYear);
  }

  @Override
  public String toString() {
    return "PostPaymentRequest{" +
        "cardNumber=" + cardNumber +
        ", expiryMonth=" + expiryMonth +
        ", expiryYear=" + expiryYear +
        ", currency='" + currency + '\'' +
        ", amount=" + amount +
        ", cvv=" + cvv +
        '}';
  }
}
