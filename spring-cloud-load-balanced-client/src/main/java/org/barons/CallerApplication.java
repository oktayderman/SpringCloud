package org.barons;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
//@LoadBalancerClient(name = "eureka-client", configuration = DiscoveryConfiguration.class) serviceDiscovery'i manual yapiyorsan
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
}