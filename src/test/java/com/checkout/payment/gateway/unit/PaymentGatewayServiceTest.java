package com.checkout.payment.gateway.unit;

import com.checkout.payment.gateway.client.BankPaymentClient;
import com.checkout.payment.gateway.enums.PaymentStatus;
import com.checkout.payment.gateway.exception.EventProcessingException;
import com.checkout.payment.gateway.mapper.PaymentMapper;
import com.checkout.payment.gateway.model.BankPostPaymentResponse;
import com.checkout.payment.gateway.model.PostPaymentRequest;
import com.checkout.payment.gateway.model.PostPaymentResponse;
import com.checkout.payment.gateway.repository.PaymentsRepository;
import com.checkout.payment.gateway.service.PaymentGatewayService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentGatewayServiceTest {

  private PaymentsRepository paymentsRepository;
  private BankPaymentClient bankPaymentClient;
  private PaymentGatewayService service;
  private PaymentMapper paymentMapper;

  @BeforeEach
  void setUp() {
    paymentsRepository = mock(PaymentsRepository.class);
    bankPaymentClient = mock(BankPaymentClient.class);
    paymentMapper = mock(PaymentMapper.class);
    service = new PaymentGatewayService(paymentsRepository, bankPaymentClient, paymentMapper);
  }

  @Test
  void shouldReturnPaymentByIdWhenExists() {
    UUID id = UUID.randomUUID();
    PostPaymentResponse expected = new PostPaymentResponse();
    expected.setId(id);

    when(paymentsRepository.get(id)).thenReturn(Optional.of(expected));

    PostPaymentResponse actual = service.getPaymentById(id);

    assertEquals(id, actual.getId());
  }

  @Test
  void shouldThrowExceptionWhenPaymentNotFound() {
    UUID id = UUID.randomUUID();
    when(paymentsRepository.get(id)).thenReturn(Optional.empty());

    assertThrows(EventProcessingException.class, () -> service.getPaymentById(id));
  }

  @Test
  void shouldProcessSuccessfulPaymentAndStoreIt() {
    PostPaymentRequest request = new PostPaymentRequest();
    request.setCardNumber("12345678901234");
    request.setExpiryMonth(12);
    request.setExpiryYear(2030);
    request.setAmount(100);

    BankPostPaymentResponse bankResponse = new BankPostPaymentResponse();
    bankResponse.setAuthorized(false);

    PostPaymentResponse paymentResponse = new PostPaymentResponse();
    paymentResponse.setId(UUID.randomUUID());
    paymentResponse.setStatus(PaymentStatus.DECLINED);
    paymentResponse.setCardNumberLastFour("1234");

    when(bankPaymentClient.processPayment(request)).thenReturn(bankResponse);
    when(paymentMapper.toPaymentResponse(request, bankResponse)).thenReturn(paymentResponse
    );
    PostPaymentResponse response = service.processPayment(request);

    assertEquals(PaymentStatus.DECLINED, response.getStatus());
    assertEquals("1234", response.getCardNumberLastFour());

    ArgumentCaptor<PostPaymentResponse> captor = ArgumentCaptor.forClass(PostPaymentResponse.class);
    verify(paymentsRepository).add(captor.capture());
    assertEquals(response.getId(), captor.getValue().getId());
  }
}