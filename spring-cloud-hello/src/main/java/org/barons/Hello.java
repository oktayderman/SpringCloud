package org.barons;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * User: Oktay CEKMEZ
 * Date: 7.06.2022
 * Time: 21:00
 */
@SpringBootApplication
@RestController
public class Hello  {



    @Value("${spring.application.name}")
    private String appName;

    public static void main(String[] args) {
        SpringApplication.run(Hello.class, args);
    }

    @RequestMapping("/hello")
    public String hello() {
        return String.format(
                "Hello from '%s'!", appName);
    }
}