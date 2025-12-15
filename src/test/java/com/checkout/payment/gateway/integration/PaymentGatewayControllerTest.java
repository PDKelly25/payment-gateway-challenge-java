package com.checkout.payment.gateway.integration;

import com.checkout.payment.gateway.controller.PaymentGatewayController;
import com.checkout.payment.gateway.model.PostPaymentRequest;
import com.checkout.payment.gateway.model.PostPaymentResponse;
import com.checkout.payment.gateway.service.PaymentGatewayService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@WebMvcTest(PaymentGatewayController.class)
class PaymentGatewayControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private PaymentGatewayService paymentGatewayService;

  @Test
  void shouldReturnPaymentById() throws Exception {
    UUID id = UUID.randomUUID();
    PostPaymentResponse response = new PostPaymentResponse();
    response.setId(id);
    response.setAmount(100);

    Mockito.when(paymentGatewayService.getPaymentById(eq(id))).thenReturn(response);

    mockMvc.perform(get("/api/v1/payments/{id}", id))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(id.toString()))
        .andExpect(jsonPath("$.amount").value(100));
  }

  @Test
  void shouldCreateNewPaymentRequest() throws Exception {
    PostPaymentResponse response = new PostPaymentResponse();
    response.setId(UUID.randomUUID());
    response.setAmount(200);

    Mockito.when(paymentGatewayService.processPayment(any(PostPaymentRequest.class)))
        .thenReturn(response);

    String json = """
            {
              "card_number": "1234567890123456",
              "expiry_month": 12,
              "expiry_year": 2030,
              "currency": "USD",
              "amount": 200,
              "cvv": "123"
            }
            """;

    mockMvc.perform(post("/api/v1/payments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(response.getId().toString()))
        .andExpect(jsonPath("$.amount").value(200));
  }
}