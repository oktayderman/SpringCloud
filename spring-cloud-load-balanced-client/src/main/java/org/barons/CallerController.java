package org.barons;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
public class CallerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CallerController.class);

    @Autowired
    Environment environment;
    @Autowired
    RestTemplate template;
    @Autowired
    WebClient.Builder webClient;

    @RequestMapping("/hello")
    public String hello() {
        String url = "http://eureka-client/hello";
        String callmeResponse = template.getForObject(url, String.class);
        return "I'm Caller running on port " + environment.getProperty("local.server.port")
                + " calling-> " + callmeResponse;
    }

    @RequestMapping("/hi")
    public Mono<String>  hi() {
        String url = "http://eureka-client/hello";

        return webClient.build().get().uri(url)
                .retrieve().bodyToMono(String.class)
                .map(greeting ->  "I'm Caller running on port " + environment.getProperty("local.server.port")
                        + " calling-> " + greeting);
    }

}