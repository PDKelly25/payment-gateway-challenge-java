package com.checkout.payment.gateway.client;

import com.checkout.payment.gateway.exception.BankResponseException;
import com.checkout.payment.gateway.model.BankPostPaymentResponse;
import com.checkout.payment.gateway.model.PostPaymentRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * Client responsible for communicating with the external bank simulator.
 * <p>
 * This component sends payment requests to the bank simulator via HTTP
 * and returns the corresponding response. It also normalizes server-side
 * errors into domain-specific exceptions that can be handled consistently
 * by the application.
 * </p>
 */
@Component
public class BankPaymentClient {
  private final String baseAddressUrl = "http://localhost:8080";
  private final RestTemplate restTemplate;

  /**
   * Constructs a new {@code BankPaymentClient} with the provided {@link RestTemplate}.
   *
   * @param restTemplate the RestTemplate used to perform HTTP requests
   */
  public BankPaymentClient(RestTemplate restTemplate){
    this.restTemplate = restTemplate;
  }

  /**
   * Sends a payment request to the bank simulator and returns the response.
   * <p>
   * This method serializes the {@link PostPaymentRequest} into JSON,
   * posts it to the bank simulator's {@code /payments} endpoint,
   * and deserializes the response into a {@link BankPostPaymentResponse}.
   * </p>
   * <p>
   * If the bank simulator responds with a server error (e.g. HTTP 5xx),
   * the error is caught and rethrown as a {@link BankResponseException}
   * with a clean, domain-specific message and status code.
   * </p>
   *
   * @param request the payment request containing card details, amount, and currency
   * @return the bank simulator's response mapped into a {@link BankPostPaymentResponse}
   * @throws BankResponseException if the bank simulator returns a server-side error
   */
  public BankPostPaymentResponse processPayment(PostPaymentRequest request){
    String paymentsEndpoint = "/payments";
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<PostPaymentRequest> entity = new HttpEntity<>(request, headers);

    // Customise Error Responses accordingly
    try {
      ResponseEntity<BankPostPaymentResponse> response = restTemplate.postForEntity(
          baseAddressUrl + paymentsEndpoint, entity, BankPostPaymentResponse.class);
      return response.getBody();
    }
    catch (HttpServerErrorException ex){
      throw new BankResponseException("Bank Response Error", ex.getStatusCode());
    }
  }
}
