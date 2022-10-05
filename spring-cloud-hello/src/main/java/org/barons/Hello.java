package org.barons;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

/**
 * User: Oktay CEKMEZ
 * Date: 7.06.2022
 * Time: 21:00
 */
@SpringBootApplication
@RestController
@EnableAsync
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
                    //creating a request
                    WebClient.RequestHeadersSpec<?> uri = webClient.get().uri("/hello");
                    //create response stream
                    WebClient.ResponseSpec spec =  uri.retrieve();
                    //retrieve sadece body ile ilgileniyorsan iyi detayli response handling icin exchange kullan
                    //If you just want to consume some api go with retrieve().
                    //If you want a better control over your response objects, headers and exceptions, go with exchange().
                    System.out.println("responseFrom:" + baseUrl + ":" + spec.bodyToMono(String.class).block());
                }catch (Exception e){
                    System.out.println("cannot get from:" +baseUrl + " exception:" + e.getClass().getSimpleName() + ":" + e.getMessage());
                }finally {
                    try {
                        Thread.sleep(1000);
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
    public String hello(HttpServletRequest request) {
        return String.format(
                "Hello from '%s' to clientIP:'%s', serverIP:'%s', ", appName, request.getRemoteAddr(),request.getLocalAddr());
        //serverPodIP:'%s' System.getenv("MY_POD_IP")
    }
}