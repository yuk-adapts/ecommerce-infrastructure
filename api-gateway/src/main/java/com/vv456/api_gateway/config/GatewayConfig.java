package com.vv456.api_gateway.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

        private static final Logger log = LoggerFactory.getLogger(GatewayConfig.class);

        @Bean
        public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
                log.info("GatewayConfig - Building RouteLocator with custom routes");
                return builder.routes()
                                // Auth Service Routes
                                .route("auth-service", r -> r
                                                .path("/api/auth/**")
                                                .filters(f -> f
                                                                .stripPrefix(0)
                                                                .filter((exchange, chain) -> {
                                                                        String method = exchange.getRequest()
                                                                                        .getMethod().name();
                                                                        String path = exchange.getRequest().getURI()
                                                                                        .getPath();
                                                                        String targetUri = "lb://auth-service";
                                                                        log.info("Route [auth-service] - Request matched: {} {}",
                                                                                        method, path);
                                                                        log.info("Route [auth-service] - Forwarding to: {}",
                                                                                        targetUri);
                                                                        return chain.filter(exchange)
                                                                                        .doOnSuccess(v -> {
                                                                                                log.info("Route [auth-service] - Request {} {} forwarded successfully",
                                                                                                                method,
                                                                                                                path);
                                                                                        })
                                                                                        .doOnError(throwable -> {
                                                                                                log.error("Route [auth-service] - Error forwarding {} {}: {}",
                                                                                                                method,
                                                                                                                path,
                                                                                                                throwable.getMessage(),
                                                                                                                throwable);
                                                                                        });
                                                                }))
                                                .uri("lb://auth-service"))

                                // Inventory Service Routes
                                .route("inventory-service", r -> r
                                                .path("/api/products/**", "/api/inventory/**")
                                                .filters(f -> f
                                                                .stripPrefix(0)
                                                                .circuitBreaker(config -> config
                                                                                .setName("inventoryServiceCircuitBreaker")
                                                                                .setFallbackUri("forward:/fallback/inventory")))
                                                .uri("lb://inventory-service"))

                                // Order Service Routes
                                .route("order-service", r -> r
                                                .path("/api/orders/**")
                                                .filters(f -> f
                                                                .stripPrefix(0)
                                                                .circuitBreaker(config -> config
                                                                                .setName("orderServiceCircuitBreaker")
                                                                                .setFallbackUri("forward:/fallback/order")))
                                                .uri("lb://order-service"))

                                // Payment Service Routes
                                .route("payment-service", r -> r
                                                .path("/api/payments/**")
                                                .filters(f -> f
                                                                .stripPrefix(0)
                                                                .circuitBreaker(config -> config
                                                                                .setName("paymentServiceCircuitBreaker")
                                                                                .setFallbackUri("forward:/fallback/payment")))
                                                .uri("lb://payment-service"))

                                // Cart Service Routes
                                .route("cart-service", r -> r
                                                .path("/api/cart/**")
                                                .filters(f -> f
                                                                .stripPrefix(0)
                                                                .circuitBreaker(config -> config
                                                                                .setName("cartServiceCircuitBreaker")
                                                                                .setFallbackUri("forward:/fallback/cart")))
                                                .uri("lb://cart-service"))

                                .build();
        }
}
