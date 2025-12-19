package com.checkout.payment.gateway.mapper;

import com.checkout.payment.gateway.enums.PaymentStatus;
import com.checkout.payment.gateway.model.BankPostPaymentResponse;
import com.checkout.payment.gateway.model.PostPaymentRequest;
import com.checkout.payment.gateway.model.PostPaymentResponse;
import org.springframework.stereotype.Component;
import java.util.UUID;

/**
 * Mapper component responsible for converting between payment request/response models.
 * <p>
 * This class transforms a {@link PostPaymentRequest} and the corresponding
 * {@link BankPostPaymentResponse} into a unified {@link PostPaymentResponse}
 * that can be returned to API clients. It ensures sensitive card details
 * are masked and that the payment status is normalized into a domain-specific
 * {@link PaymentStatus}.
 * </p>
 */
@Component
public class PaymentMapper {

  /**
   * Maps a {@link PostPaymentRequest} and {@link BankPostPaymentResponse}
   * into a {@link PostPaymentResponse}.
   * <p>
   * A new unique identifier is generated for the payment response,
   * the card number is masked to expose only the last four digits,
   * and the payment status is determined based on the bank's authorization result.
   * </p>
   *
   * @param paymentRequest the original payment request containing card details and transaction info
   * @param bankPostPaymentResponse the bank simulator's response indicating authorization outcome
   * @return a {@link PostPaymentResponse} containing normalized payment details
   */
  public PostPaymentResponse toPaymentResponse(
      PostPaymentRequest paymentRequest,
      BankPostPaymentResponse bankPostPaymentResponse
  ) {
    PostPaymentResponse postPaymentResponse = new PostPaymentResponse();

    postPaymentResponse.setId(UUID.randomUUID());
    postPaymentResponse.setStatus(bankPostPaymentResponse.isAuthorized()
        ? PaymentStatus.AUTHORIZED : PaymentStatus.DECLINED);
    postPaymentResponse.setCardNumberLastFour(maskCardNumber(paymentRequest.getCardNumber()));
    postPaymentResponse.setExpiryMonth(paymentRequest.getExpiryMonth());
    postPaymentResponse.setExpiryYear(paymentRequest.getExpiryYear());
    postPaymentResponse.setCurrency(paymentRequest.getCurrency());
    postPaymentResponse.setAmount(paymentRequest.getAmount());

    return postPaymentResponse;
  }

  /**
   * Extracts and returns the last four digits of a card number.
   * <p>
   * This method ensures sensitive card information is not exposed
   * in API responses, while still allowing merchants to identify
   * the card used for the transaction.
   * </p>
   *
   * @param cardNumber the full card number provided in the payment request
   * @return a string containing only the last four digits of the card number
   */
  private String maskCardNumber(String cardNumber) {
    // create cardNumberLastFour substring
    return cardNumber.substring(cardNumber.length() - 4);
  }
}
