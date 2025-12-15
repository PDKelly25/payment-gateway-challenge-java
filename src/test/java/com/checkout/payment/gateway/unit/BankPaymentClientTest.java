package com.checkout.payment.gateway.unit;

import com.checkout.payment.gateway.client.BankPaymentClient;
import com.checkout.payment.gateway.model.BankPostPaymentResponse;
import com.checkout.payment.gateway.model.PostPaymentRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@SpringBootTest
class BankPaymentClientMockServerTest {

  @Autowired
  private RestTemplate restTemplate;

  private BankPaymentClient client;
  private MockRestServiceServer mockServer;

  @BeforeEach
  void setUp() {
    client = new BankPaymentClient(restTemplate);
    mockServer = MockRestServiceServer.createServer(restTemplate);
  }

  @Test
  void shouldReturnApprovedResponse() {
    mockServer.expect(requestTo("http://localhost:8080/payments"))
        .andExpect(method(HttpMethod.POST))
        .andRespond(withSuccess(
            "{\"authorized\":true, \"authorization_code\": "
                + "\"0bb07405-6d44-4b50-a14f-7ae0beff13ad\"}", MediaType.APPLICATION_JSON));

    PostPaymentRequest request = new PostPaymentRequest();
    request.setCardNumber("12345678901234");

    BankPostPaymentResponse response = client.processPayment(request);
    assertEquals("0bb07405-6d44-4b50-a14f-7ae0beff13ad", response.getAuthorizationCode());
  }

  @Test
  void shouldHandleSimulator503() {
    mockServer.expect(requestTo("http://localhost:8080/payments"))
        .andRespond(withStatus(HttpStatus.SERVICE_UNAVAILABLE));

    PostPaymentRequest request = new PostPaymentRequest();
    request.setCardNumber("1234567890");

    assertThrows(HttpServerErrorException.ServiceUnavailable.class,
        () -> client.processPayment(request));
  }
}