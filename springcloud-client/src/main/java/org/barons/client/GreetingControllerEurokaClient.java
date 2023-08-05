package org.barons.client;

import brave.Tracing;
import brave.propagation.B3Propagation;
import com.netflix.discovery.EurekaClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.*;
import org.springframework.context.annotation.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

/**
 * User: Oktay CEKMEZ
 * Date: 7.06.2022
 * Time: 21:00
 */
@SpringBootApplication
@RestController
public class GreetingControllerEurokaClient implements GreetingController {
    Logger logger = LoggerFactory.getLogger(GreetingControllerEurokaClient.class);
    @Autowired
    @Lazy
    private EurekaClient eurekaClient;

    @Value("${spring.application.name}")
    private String appName;

    public static void main(String[] args) {
        SpringApplication.run(GreetingControllerEurokaClient.class, args);
    }

    @Bean
    public Tracing braveTracing() {
        return Tracing.newBuilder()
                .propagationFactory(B3Propagation.newFactoryBuilder().injectFormat(B3Propagation.Format.MULTI).build())
                .build();
    }
    @Override
    public String greeting(@RequestHeader MultiValueMap<String, String> headers) {
        logger.info("headers:\n" + headers.toString());
        return String.format(
                "Hello from '%s'!", eurekaClient.getApplication(appName).getName());
    }
}