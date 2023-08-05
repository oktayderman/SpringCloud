package org.barons.client;


import com.netflix.discovery.EurekaClient;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.*;
import org.springframework.context.annotation.*;
import org.springframework.web.bind.annotation.*;

/**
 * User: Oktay CEKMEZ
 * Date: 7.06.2022
 * Time: 21:00
 */
@SpringBootApplication
@RestController
public class GreetingControllerEurokaClient {

    @Autowired
    @Lazy
    private EurekaClient eurekaClient;

    @Value("${spring.application.name}")
    private String appName;

    public static void main(String[] args) {
        SpringApplication.run(GreetingControllerEurokaClient.class, args);
    }


}