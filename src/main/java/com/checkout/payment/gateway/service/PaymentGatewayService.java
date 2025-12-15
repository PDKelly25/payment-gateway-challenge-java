package com.checkout.payment.gateway.service;

import com.checkout.payment.gateway.client.BankPaymentClient;
import com.checkout.payment.gateway.enums.PaymentStatus;
import com.checkout.payment.gateway.exception.EventProcessingException;
import com.checkout.payment.gateway.model.BankPostPaymentResponse;
import com.checkout.payment.gateway.model.PostPaymentRequest;
import com.checkout.payment.gateway.model.PostPaymentResponse;
import com.checkout.payment.gateway.repository.PaymentsRepository;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class  PaymentGatewayService {

  private static final Logger LOG = LoggerFactory.getLogger(PaymentGatewayService.class);

  private final PaymentsRepository paymentsRepository;
  private final BankPaymentClient bankPaymentClient;

  public PaymentGatewayService(PaymentsRepository paymentsRepository, BankPaymentClient bankPaymentClient) {
    this.paymentsRepository = paymentsRepository;
    this.bankPaymentClient = bankPaymentClient;
  }

  public PostPaymentResponse getPaymentById(UUID id) {
    LOG.debug("Requesting access to payment with ID {}", id);
    return paymentsRepository.get(id).orElseThrow(() -> new EventProcessingException("Invalid ID"));
  }

  public PostPaymentResponse processPayment(PostPaymentRequest paymentRequest) {
    BankPostPaymentResponse bankPostPaymentResponse = bankPaymentClient.processPayment(
        paymentRequest);

    // create cardNumberLastFour substring
    int cardNumberLastFourLength = 4;
    String cardNumber = paymentRequest.getCardNumber();
    String cardNumberLastFour = cardNumber.substring(
        cardNumber.length() - cardNumberLastFourLength);

    // instantiate PostPaymentResponse
    PostPaymentResponse postPaymentResponse = new PostPaymentResponse();

    // set PostPaymentResponse params (using corresponding PostPaymentRequest params)
    postPaymentResponse.setId(UUID.randomUUID());
    postPaymentResponse.setStatus(bankPostPaymentResponse.isAuthorized()
        ? PaymentStatus.AUTHORIZED : PaymentStatus.DECLINED);
    postPaymentResponse.setCardNumberLastFour(cardNumberLastFour);
    postPaymentResponse.setExpiryMonth(paymentRequest.getExpiryMonth());
    postPaymentResponse.setExpiryYear(paymentRequest.getExpiryYear());
    postPaymentResponse.setCurrency(paymentRequest.getCurrency());
    postPaymentResponse.setAmount(paymentRequest.getAmount());

    // Only store postPaymentResponse in PaymentsRepository if authorized from Bank Simulator
    if (postPaymentResponse.getStatus().equals(PaymentStatus.DECLINED)){
      throw new EventProcessingException("Payment Declined");
    }
    paymentsRepository.add(postPaymentResponse);
    return postPaymentResponse;
  }
}
