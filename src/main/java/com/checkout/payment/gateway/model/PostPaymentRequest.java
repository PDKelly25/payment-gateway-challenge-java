package com.checkout.payment.gateway.model;

import com.checkout.payment.gateway.enums.ISOCurrencyCode;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.cglib.core.Local;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.YearMonth;

public class PostPaymentRequest implements Serializable {

  @JsonProperty("card_number")
  @NotNull(message="Card Number is required")
  @Size(min = 14, max = 19, message = "Card Number must be between 14 - 19 characters long")
  @Pattern(regexp = "\\d+", message = "Card Number must only contain numeric characters")
  private String cardNumber;

  @JsonProperty("expiry_month")
  @NotNull(message="Expiry Month is required")
  @Min(value = 1, message = "Expiry Month should (inclusively) be between 1 - 12")
  @Max(value = 12, message = "Expiry Month should (inclusively) be between 1 - 12")
  private int expiryMonth;

  @JsonProperty("expiry_year")
  @NotNull(message="Expiry Year is required")
  private int expiryYear;

  // currency and amount cannot be null
  private ISOCurrencyCode currency;
  private int amount;

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
