package com.checkout.payment.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the Payment Gateway application.
 * <p>
 * This class bootstraps the Spring Boot application, enabling auto-configuration,
 * component scanning, and configuration properties. It serves as the starting
 * point for the payment gateway service, initializing the application context
 * and launching the embedded server.
 * </p>
 */
@SpringBootApplication
public class PaymentGatewayApplication {

  /**
   * Main method used to launch the Payment Gateway application.
   * <p>
   * This method delegates to {@link SpringApplication#run(Class, String...)}
   * to start the Spring Boot application, initializing all configured beans
   * and starting the embedded web server.
   * </p>
   *
   * @param args command-line arguments passed to the application
   */
  public static void main(String[] args) {
    SpringApplication.run(PaymentGatewayApplication.class, args);
  }

}
