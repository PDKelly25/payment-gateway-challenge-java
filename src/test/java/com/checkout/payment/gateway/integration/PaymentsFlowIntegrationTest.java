package com.checkout.payment.gateway.integration;

import com.checkout.payment.gateway.enums.PaymentStatus;
import com.checkout.payment.gateway.model.BankPostPaymentResponse;
import com.checkout.payment.gateway.model.PostPaymentResponse;
import com.checkout.payment.gateway.repository.PaymentsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PaymentsFlowIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private PaymentsRepository paymentsRepository;

  @Autowired
  private RestTemplate restTemplate;

  private MockRestServiceServer mockServer;

  @BeforeEach
  void setUp() {
    mockServer = MockRestServiceServer.createServer(restTemplate);
  }

  @Test
  void shouldProcessPaymentEndToEnd() throws Exception {
    // Mock the bank simulator response
    BankPostPaymentResponse bankResponse = new BankPostPaymentResponse();
    bankResponse.setAuthorized(true);
    bankResponse.setAuthorizationCode("random_auth_code");

    mockServer.expect(requestTo("http://localhost:8080/payments"))
        .andExpect(method(HttpMethod.POST))
        .andRespond(withSuccess(
            "{\"authorized\":true, \"authorization_code\": "
                + "\"0bb07405-6d44-4b50-a14f-7ae0beff13ad\"}", MediaType.APPLICATION_JSON));

    String json = """
            {
              "card_number": "1234567890123456",
              "expiry_month": 12,
              "expiry_year": 2030,
              "currency": "USD",
              "amount": 150,
              "cvv": "123"
            }
            """;

    // Perform POST request to controller
    String responseBody = mockMvc.perform(post("http://localhost:8090/api/v1/payments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.amount").value(150))
        .andExpect(jsonPath("$.currency").value("USD"))
        .andExpect(jsonPath("$.expiry_month").value(12))
        .andExpect(jsonPath("$.expiry_year").value(2030))
        .andExpect(jsonPath("$.card_number_last_four").value("3456"))
        .andExpect(jsonPath("$.status").value("Authorized"))
        .andReturn()
        .getResponse()
        .getContentAsString();

    // Extract ID from response
    com.fasterxml.jackson.databind.JsonNode node =
        new com.fasterxml.jackson.databind.ObjectMapper().readTree(responseBody);
    UUID id = UUID.fromString(node.get("id").asText());

    // Verify repository contains the stored payment
    Optional<PostPaymentResponse> stored = paymentsRepository.get(id);
    assertTrue(stored.isPresent());
    assertEquals(PaymentStatus.AUTHORIZED, stored.get().getStatus());
    assertEquals("3456", stored.get().getCardNumberLastFour());
  }

  @Test
  void shouldProcessError() throws Exception {
    // Mock the bank simulator error response
    mockServer.expect(ExpectedCount.once(),
            requestTo("http://localhost:8080/payments"))
        .andRespond(withStatus(HttpStatus.SERVICE_UNAVAILABLE));

    String json = """
            {
              "card_number": "1234567890123450",
              "expiry_month": 6,
              "expiry_year": 2030,
              "currency": "GBP",
              "amount": 150,
              "cvv": "123"
            }
            """;

    // Perform POST request to controller
    mockMvc.perform(post("http://localhost:8090/api/v1/payments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isServiceUnavailable()) // or isServiceUnavailable(), depending on your mapping
        .andExpect(jsonPath("$.message").value("Bank Response Error"));
  }
}

