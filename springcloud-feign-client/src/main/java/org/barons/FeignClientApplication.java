package org.barons;



import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.time.Duration;

@SpringBootApplication
@EnableFeignClients

public class FeignClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(FeignClientApplication.class, args);
    }


    //TODO gateway ve euroka server(cluster oluyor alttaki config'leri acacagiz)  bitane mi olur ?
    //eureka.client.register-with-eureka=false
    //eureka.client.fetch-registry=false
}