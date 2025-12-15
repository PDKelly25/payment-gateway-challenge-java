package com.checkout.payment.gateway.integration;

import com.checkout.payment.gateway.enums.PaymentStatus;
import com.checkout.payment.gateway.model.PostPaymentResponse;
import com.checkout.payment.gateway.repository.PaymentsRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PaymentsRepositoryIntegrationTest {

  @Autowired
  private PaymentsRepository paymentsRepository;

  @Test
  void shouldAddAndRetrievePayment() {
    PostPaymentResponse payment = new PostPaymentResponse();
    UUID id = UUID.randomUUID();
    payment.setId(id);
    payment.setAmount(100);
    payment.setStatus(PaymentStatus.AUTHORIZED);

    paymentsRepository.add(payment);

    Optional<PostPaymentResponse> retrieved = paymentsRepository.get(id);
    assertTrue(retrieved.isPresent());
    assertEquals(100, retrieved.get().getAmount());
    assertEquals(PaymentStatus.AUTHORIZED, retrieved.get().getStatus());
  }

  @Test
  void shouldReturnEmptyWhenPaymentNotFound() {
    UUID randomId = UUID.randomUUID();
    Optional<PostPaymentResponse> result = paymentsRepository.get(randomId);
    assertTrue(result.isEmpty());
  }

  @Test
  void shouldStoreMultiplePaymentsIndependently() {
    PostPaymentResponse payment1 = new PostPaymentResponse();
    UUID id1 = UUID.randomUUID();
    payment1.setId(id1);
    payment1.setAmount(50);

    PostPaymentResponse payment2 = new PostPaymentResponse();
    UUID id2 = UUID.randomUUID();
    payment2.setId(id2);
    payment2.setAmount(75);

    paymentsRepository.add(payment1);
    paymentsRepository.add(payment2);

    assertEquals(50, paymentsRepository.get(id1).get().getAmount());
    assertEquals(75, paymentsRepository.get(id2).get().getAmount());
  }
}