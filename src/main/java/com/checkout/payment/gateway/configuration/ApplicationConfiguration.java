package com.checkout.payment.gateway.configuration;

import java.time.Duration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Application-wide configuration class for defining reusable Spring beans.
 * <p>
 * This configuration provides a customized {@link RestTemplate} bean
 * with sensible defaults for connection and read timeouts. The bean
 * can be injected into other components (such as clients) to perform
 * HTTP requests with consistent timeout behavior.
 * </p>
 */
@Configuration
public class ApplicationConfiguration {

  /**
   * Creates and configures a {@link RestTemplate} bean.
   * <p>
   * The {@link RestTemplate} is built using {@link RestTemplateBuilder}
   * with a connection timeout and read timeout of 10 seconds each.
   * These settings ensure that HTTP calls fail fast if the remote
   * service is unavailable or unresponsive, rather than hanging indefinitely.
   * </p>
   *
   * @param builder the builder used to construct and configure the RestTemplate
   * @return a {@link RestTemplate} instance with 10-second connection and read timeouts
   */
  @Bean
  public RestTemplate restTemplate(RestTemplateBuilder builder) {
    return builder
        .setConnectTimeout(Duration.ofMillis(10000))
        .setReadTimeout(Duration.ofMillis(10000))
        .build();
  }
}

