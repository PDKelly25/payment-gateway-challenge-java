package com.checkout.payment.gateway.service;

import com.checkout.payment.gateway.client.BankPaymentClient;
import com.checkout.payment.gateway.exception.EventProcessingException;
import com.checkout.payment.gateway.mapper.PaymentMapper;
import com.checkout.payment.gateway.model.BankPostPaymentResponse;
import com.checkout.payment.gateway.model.PostPaymentRequest;
import com.checkout.payment.gateway.model.PostPaymentResponse;
import com.checkout.payment.gateway.repository.PaymentsRepository;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service layer responsible for orchestrating payment processing operations.
 * <p>
 * This class acts as the core business logic for the payment gateway.
 * It interacts with the {@link BankPaymentClient} to process payments,
 * delegates mapping logic to {@link PaymentMapper}, and manages persistence
 * through the {@link PaymentsRepository}. It ensures that payment requests
 * and responses are validated, normalized, and stored appropriately.
 * </p>
 */
@Service
public class  PaymentGatewayService {

  private static final Logger LOG = LoggerFactory.getLogger(PaymentGatewayService.class);

  private final PaymentsRepository paymentsRepository;
  private final BankPaymentClient bankPaymentClient;
  private final PaymentMapper paymentMapper;

  /**
   * Constructs a new {@code PaymentGatewayService} with the required dependencies.
   *
   * @param paymentsRepository the repository used to store and retrieve payments
   * @param bankPaymentClient the client used to communicate with the bank simulator
   * @param paymentMapper the mapper used to transform requests and bank responses into API responses
   */
  public PaymentGatewayService(PaymentsRepository paymentsRepository, BankPaymentClient bankPaymentClient,
      PaymentMapper paymentMapper) {
    this.paymentsRepository = paymentsRepository;
    this.bankPaymentClient = bankPaymentClient;
    this.paymentMapper = paymentMapper;
  }

  /**
   * Retrieves a payment by its unique identifier.
   * <p>
   * If the payment cannot be found, an {@link EventProcessingException} is thrown.
   * </p>
   *
   * @param id the unique identifier of the payment
   * @return the {@link PostPaymentResponse} associated with the given ID
   * @throws EventProcessingException if no payment exists with the given ID
   */
  public PostPaymentResponse getPaymentById(UUID id) {
    LOG.debug("Requesting access to payment with ID {}", id);
    return paymentsRepository.get(id).orElseThrow(() -> new EventProcessingException("Invalid ID"));
  }

  /**
   * Processes a new payment request.
   * <p>
   * This method sends the request to the bank simulator, uses the
   * {@link PaymentMapper} to construct a {@link PostPaymentResponse},
   * and persists the response in the repository. If the bank declines
   * the payment, the response will reflect that status but is still
   * returned to the client.
   * </p>
   *
   * @param paymentRequest the {@link PostPaymentRequest} containing card and transaction details
   * @return the {@link PostPaymentResponse} containing the outcome of the payment
   */
  public PostPaymentResponse processPayment(PostPaymentRequest paymentRequest) {
    BankPostPaymentResponse bankPostPaymentResponse = bankPaymentClient.processPayment(
        paymentRequest);

    PostPaymentResponse postPaymentResponse = paymentMapper.toPaymentResponse(
        paymentRequest, bankPostPaymentResponse);

    paymentsRepository.add(postPaymentResponse);
    return postPaymentResponse;
  }
}
