package org.barons;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class CallerApplication {

   public static void main(String[] args) {
      SpringApplication.run(CallerApplication.class, args);
   }

   @LoadBalanced
   @Bean
   RestTemplate template() {
      return new RestTemplate();
   }

   @LoadBalanced
   @Bean
   WebClient.Builder webClientBuilder() {
      return WebClient.builder();
   }

   //eger instance'lari kendin vereceksen @LoadBalancerClient(name = "eureka-client", configuration = DiscoveryConfiguration.class)
}