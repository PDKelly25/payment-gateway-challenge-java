package com.checkout.payment.gateway.unit;

import com.checkout.payment.gateway.enums.ISOCurrencyCode;
import com.checkout.payment.gateway.mapper.PaymentMapper;
import com.checkout.payment.gateway.model.BankPostPaymentResponse;
import com.checkout.payment.gateway.model.PostPaymentRequest;
import com.checkout.payment.gateway.model.PostPaymentResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PaymentMapperTest {

  private final PaymentMapper paymentMapper = new PaymentMapper();

  @Test
  void shouldExtractLastFourDigitsCorrectly() {
    PostPaymentRequest request = new PostPaymentRequest();
    request.setCardNumber("9876543210000");
    request.setAmount(1050);
    request.setCurrency(ISOCurrencyCode.EUR);
    request.setExpiryYear(2030);
    request.setExpiryMonth(7);

    BankPostPaymentResponse bankResponse = new BankPostPaymentResponse();
    bankResponse.setAuthorized(true);
    bankResponse.setAuthorizationCode("random_auth_code");

    PostPaymentResponse mappedResponse = paymentMapper.toPaymentResponse(request, bankResponse);
    assertEquals("0000", mappedResponse.getCardNumberLastFour());
  }
}
