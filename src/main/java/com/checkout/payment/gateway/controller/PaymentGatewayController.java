package com.checkout.payment.gateway.controller;

import com.checkout.payment.gateway.model.PostPaymentRequest;
import com.checkout.payment.gateway.model.PostPaymentResponse;
import com.checkout.payment.gateway.service.PaymentGatewayService;
import java.util.UUID;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for handling payment gateway operations.
 * <p>
 * This controller exposes endpoints for creating new payment requests
 * and retrieving existing payment events by their unique identifier.
 * It delegates business logic to the {@link PaymentGatewayService}
 * and ensures responses are serialized into JSON for merchant consumption.
 * </p>
 */
@RestController
@RequestMapping("/api/v1/payments")
public class PaymentGatewayController {

  private final PaymentGatewayService paymentGatewayService;

  /**
   * Constructs a new {@code PaymentGatewayController} with the provided service.
   *
   * @param paymentGatewayService the service responsible for processing and retrieving payments
   */
  public PaymentGatewayController(PaymentGatewayService paymentGatewayService) {
    this.paymentGatewayService = paymentGatewayService;
  }

  /**
   * Retrieves a payment event by its unique identifier.
   * <p>
   * This endpoint allows merchants to query the status or details of a
   * previously submitted payment request using its {@link UUID}.
   * </p>
   *
   * @param id the unique identifier of the payment event
   * @return a {@link ResponseEntity} containing the {@link PostPaymentResponse} and HTTP 200 status
   */
  @GetMapping("/{id}")
  public ResponseEntity<PostPaymentResponse> getPostPaymentEventById(@PathVariable UUID id) {
    return new ResponseEntity<>(paymentGatewayService.getPaymentById(id), HttpStatus.OK);
  }

  /**
   * Submits a new payment request to the gateway.
   * <p>
   * This endpoint accepts a JSON payload representing the {@link PostPaymentRequest},
   * validates it, and forwards it to the {@link PaymentGatewayService} for processing.
   * The resulting {@link PostPaymentResponse} is returned to the merchant.
   * </p>
   *
   * @param request the payment request containing card details, amount, and currency
   * @return a {@link ResponseEntity} containing the {@link PostPaymentResponse} and HTTP 200 status
   */
  @PostMapping()
  public ResponseEntity<PostPaymentResponse> newPaymentRequest(
      @Valid @RequestBody PostPaymentRequest request){
    return new ResponseEntity<>(paymentGatewayService.processPayment(request), HttpStatus.OK);
  }
}
