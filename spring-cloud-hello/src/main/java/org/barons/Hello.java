package org.barons;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import javax.annotation.PostConstruct;

/**
 * User: Oktay CEKMEZ
 * Date: 7.06.2022
 * Time: 21:00
 */
@SpringBootApplication
@RestController
public class Hello {
    private boolean closed = false;
    WebClient webClient;

    @Value("${spring.application.name}")
    private String appName;

    @Value("${client.app}")
    private String clientApp;
    private String baseUrl;

    @PostConstruct
    void initialize() {
        baseUrl = "http://" + clientApp;
        webClient = WebClient.create(baseUrl);
        new Thread(() -> {
            while(!closed){
                try{
                   WebClient.ResponseSpec spec =  webClient.get().uri("/hello").retrieve();
                    System.out.println("responseFrom:" + baseUrl + ":" + spec.bodyToMono(String.class).block());
                }catch (Exception e){
                    System.out.println("cannot get from:" +baseUrl + " exception:" + e.getClass().getSimpleName() + ":" + e.getMessage());
                }finally {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }).start();
    }


    public static void main(String[] args) {
        SpringApplication.run(Hello.class, args);


    }

    @RequestMapping("/hello")
    public String hello() {
        return String.format(
                "Hello from '%s'!", appName);
    }
}