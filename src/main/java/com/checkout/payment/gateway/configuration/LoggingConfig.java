package com.checkout.payment.gateway.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

/**
 * Configuration class for enabling HTTP request logging within the application.
 * <p>
 * This configuration defines a {@link CommonsRequestLoggingFilter} bean
 * that logs incoming HTTP requests. It can be customized to include query
 * strings, payloads, headers, and other request details. Logging is useful
 * for debugging, auditing, and monitoring API traffic.
 * </p>
 */
@Configuration
public class LoggingConfig {

  /**
   * Creates and configures a {@link CommonsRequestLoggingFilter} bean.
   * <p>
   * The filter is set to include query strings in the log output and
   * prefixes each log entry with {@code "REQUEST DATA : "}. Additional
   * options such as payload and headers can be enabled if needed.
   * </p>
   *
   * @return a configured {@link CommonsRequestLoggingFilter} instance
   */
  @Bean
  public CommonsRequestLoggingFilter requestLoggingFilter() {
    CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
    filter.setIncludeQueryString(true);
//    The following commented lines includes payload and headers in logs:
//    filter.setIncludePayload(true);
//    filter.setMaxPayloadLength(10000);
//    filter.setIncludeHeaders(true);
    filter.setAfterMessagePrefix("REQUEST DATA : ");
    return filter;
  }
}