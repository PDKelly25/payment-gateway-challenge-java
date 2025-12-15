package com.checkout.payment.gateway.client;

import com.checkout.payment.gateway.model.BankPostPaymentResponse;
import com.checkout.payment.gateway.model.PostPaymentRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class BankPaymentClient {
  private final String baseAddressUrl = "http://localhost:8080";
  private final RestTemplate restTemplate;

  public BankPaymentClient(RestTemplate restTemplate){
    this.restTemplate = restTemplate;
  }

  public BankPostPaymentResponse processPayment(PostPaymentRequest request){
    String paymentsEndpoint = "/payments";
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<PostPaymentRequest> entity = new HttpEntity<>(request, headers);
    ResponseEntity<BankPostPaymentResponse> response = restTemplate.postForEntity(
        baseAddressUrl + paymentsEndpoint, entity, BankPostPaymentResponse.class);

    return response.getBody();
  }
}
