package org.barons;


import brave.Tracing;
import brave.propagation.B3Propagation;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.Duration;

@SpringBootApplication
@EnableFeignClients

public class FeignClientApplication {





    @Bean
    public Tracing braveTracing() {
        return Tracing.newBuilder()
                .propagationFactory(B3Propagation.newFactoryBuilder().injectFormat(B3Propagation.Format.MULTI).build())
                .build();
    }

    public static void main(String[] args) {
        SpringApplication.run(FeignClientApplication.class, args);
    }



    //TODO gateway ve euroka server(cluster oluyor alttaki config'leri acacagiz)  bitane mi olur ?
    //eureka.client.register-with-eureka=false
    //eureka.client.fetch-registry=false
}