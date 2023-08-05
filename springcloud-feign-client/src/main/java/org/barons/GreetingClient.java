package org.barons;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * User: Oktay CEKMEZ
 * Date: 16.06.2022
 * Time: 20:16
 */
@FeignClient(value = "spring-cloud-eureka-client")//fallback = GreetingClientFallback.class
public interface GreetingClient {
    @RequestMapping("/greeting")
    String greeting();
}

/*
@Component
class GreetingClientFallback implements GreetingClient{

    @Override
    public String greeting() {
        return "Fallbacl Erro101";
    }
}*/