package com.checkout.payment.gateway.repository;

import com.checkout.payment.gateway.model.PostPaymentResponse;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

/**
 * Repository for storing and retrieving payment responses.
 * <p>
 * This in-memory repository maintains a simple {@link HashMap} of payment
 * transactions keyed by their unique identifier. It provides methods to
 * add new payments and retrieve existing ones. In a production system,
 * this would typically be replaced by a persistent data store.
 * </p>
 */
@Repository
public class PaymentsRepository {

  /**
   * Internal storage for payment responses, keyed by payment ID.
   */
  private final HashMap<UUID, PostPaymentResponse> payments = new HashMap<>();

  /**
   * Adds a new payment response to the repository.
   *
   * @param payment the {@link PostPaymentResponse} to store
   */
  public void add(PostPaymentResponse payment) {
    payments.put(payment.getId(), payment);
  }

  /**
   * Retrieves a payment response by its unique identifier.
   *
   * @param id the unique identifier of the payment
   * @return an {@link Optional} containing the {@link PostPaymentResponse}
   *         if found, or empty if no payment exists with the given ID
   */
  public Optional<PostPaymentResponse> get(UUID id) {
    return Optional.ofNullable(payments.get(id));
  }

}
