package org.barons;

import brave.Tracer;
import brave.handler.MutableSpan;
import brave.handler.SpanHandler;
import brave.propagation.TraceContext;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.slf4j.Logger;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * User: Oktay CEKMEZ
 * Date: 17.06.2022
 * Time: 11:33
 */
@Configuration
public class CircuitBreakerConfiguration {

    //see BraveAutoConfiguration
    @Bean(name = {"logSpanHandler"})
    protected SpanHandler getLogSpanHandler() {
        return new SpanHandler() {
            private final Logger logger = org.slf4j.LoggerFactory.getLogger(Tracer.class);

            @Override
            public boolean end(TraceContext context, MutableSpan span, Cause cause) {
                logger.info(span.toString());
                return true;
            }
        };
    }

    @Bean
    public Customizer<Resilience4JCircuitBreakerFactory> defaultCustomizer() {
        return factory -> factory.configureDefault(
                id -> new Resilience4JConfigBuilder(id)
                        .timeLimiterConfig(TimeLimiterConfig.custom()
                                .timeoutDuration(Duration.ofSeconds(4)).build())
                        .circuitBreakerConfig(CircuitBreakerConfig.custom().minimumNumberOfCalls(5).build())
                        .build());
    }
}
