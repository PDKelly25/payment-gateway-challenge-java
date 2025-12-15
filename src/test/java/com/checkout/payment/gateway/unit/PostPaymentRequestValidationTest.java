package com.checkout.payment.gateway.unit;

import com.checkout.payment.gateway.enums.ISOCurrencyCode;
import com.checkout.payment.gateway.model.PostPaymentRequest;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class PostPaymentRequestTest {
  private Validator validator;

  @BeforeEach
  void setUp() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  // 1. Card Number

  @Test
  void shouldFailWhenCardNumberIsNull() {
    PostPaymentRequest req = validRequest();
    req.setCardNumber(null);

    var violations = validator.validate(req);
    assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Card Number is required")));
  }

  @Test
  void shouldFailWhenCardNumberTooShort() {
    PostPaymentRequest req = validRequest();
    req.setCardNumber("1234567"); // 7 digits

    var violations = validator.validate(req);
    assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("between 14 - 19")));
  }

  @Test
  void shouldFailWhenCardNumberContainsLetters() {
    PostPaymentRequest req = validRequest();
    req.setCardNumber("1234abcdefg5678");

    var violations = validator.validate(req);
    assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("numeric characters")));
  }

  @Test
  void shouldPassWhenCardNumberIsValid() {
    PostPaymentRequest req = validRequest();
    req.setCardNumber("5533785573947297438");

    var violations = validator.validate(req);
    assertTrue(violations.isEmpty());
  }

  // 2. Expiry Month

  @Test
  void shouldFailWhenExpiryMonthOutOfRange() {
    PostPaymentRequest req = validRequest();
    req.setExpiryMonth(13);

    var violations = validator.validate(req);
    assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("between 1 - 12")));
  }

  // 3. Expiry Year

  @Test
  void shouldFailWhenExpiryYearIsZero() {
    PostPaymentRequest req = validRequest();
    req.setExpiryYear(0);

    var violations = validator.validate(req);
    assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Date must be in the future")));
  }

//  4. CVV

  @Test
  void shouldFailWhenCvvIsNull() {
    PostPaymentRequest req = validRequest();
    req.setCvv(null);

    var violations = validator.validate(req);
    assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("CVV is required")));
  }

  @Test
  void shouldFailWhenCvvTooShort() {
    PostPaymentRequest req = validRequest();
    req.setCvv("12");

    var violations = validator.validate(req);
    assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("3-4 characters")));
  }

  @Test
  void shouldFailWhenCvvContainsLetters() {
    PostPaymentRequest req = validRequest();
    req.setCvv("12A");

    var violations = validator.validate(req);
    assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("numeric characters")));
  }

  //  5. Expiry Date Validity

  @Test
  void shouldFailWhenExpiryDateIsInPast() {
    PostPaymentRequest req = validRequest();
    req.setExpiryMonth(1);
    req.setExpiryYear(2020);

    var violations = validator.validate(req);
    assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Expiry Date must be in the future")));
  }

  private PostPaymentRequest validRequest() {
    PostPaymentRequest req = new PostPaymentRequest();
    req.setCardNumber("12345678901234");
    req.setExpiryMonth(12);
    req.setExpiryYear(2030);
    req.setCurrency(ISOCurrencyCode.USD);
    req.setAmount(100);
    req.setCvv("123");
    return req;
  }
}
