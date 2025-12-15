package com.checkout.payment.gateway.integration;

import com.checkout.payment.gateway.enums.PaymentStatus;
import com.checkout.payment.gateway.model.BankPostPaymentResponse;
import com.checkout.payment.gateway.model.PostPaymentRequest;
import com.checkout.payment.gateway.model.PostPaymentResponse;
import com.checkout.payment.gateway.repository.PaymentsRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PaymentsFlowIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private PaymentsRepository paymentsRepository;

  @MockBean
  private com.checkout.payment.gateway.client.BankPaymentClient bankPaymentClient;

  @Test
  void shouldProcessPaymentEndToEnd() throws Exception {
    // Mock the bank simulator response
    BankPostPaymentResponse bankResponse = new BankPostPaymentResponse();
    bankResponse.setAuthorized(true);
    when(bankPaymentClient.processPayment(any(PostPaymentRequest.class)))
        .thenReturn(bankResponse);

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
    String responseBody = mockMvc.perform(post("http://localhost:8080/api/v1/payments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value("Authorized"))
        .andExpect(jsonPath("$.amount").value(150))
        .andReturn()
        .getResponse()
        .getContentAsString();

    // Extract ID from response
    // (simplified: parse manually or with ObjectMapper)
    com.fasterxml.jackson.databind.JsonNode node =
        new com.fasterxml.jackson.databind.ObjectMapper().readTree(responseBody);
    UUID id = UUID.fromString(node.get("id").asText());

    // Verify repository contains the stored payment
    Optional<PostPaymentResponse> stored = paymentsRepository.get(id);
    assertTrue(stored.isPresent());
    assertEquals(PaymentStatus.AUTHORIZED, stored.get().getStatus());
    assertEquals("3456", stored.get().getCardNumberLastFour());
  }
}