package org.barons.cloud.client.eureka;

import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.client.RestTemplate;

import jakarta.annotation.PostConstruct;
import java.util.List;

/**
 * User: Oktay CEKMEZ
 * Date: ${DATE}
 * Time: ${TIME}
 */
@SpringBootApplication
public class EurekaClientApplication {
    //http://localhost:8761/eureka/apps/appName to get details for the app from eureka server
    static EurekaClientApplication instance;
    @Autowired
    ApplicationInfoManager appInfoManager;

    EurekaClientApplication() {
        EurekaClientApplication.instance = this;
    }

    //todoProd hearthbeat gonderemiyorken(kill olurken veya suspend'de) euroka ne yapiyor 10sn icinde anlamali
    @LoadBalanced
    @Bean
    RestTemplate template() {
        return new RestTemplate();
    }


    public static void main(String[] args) {
        SpringApplication.run(EurekaClientApplication.class, args);
    }

    //InstanceInfoReplicator.run ile degisen statü varsa yeni statü eureka server'a yollanir(instanceInfo)  --> instanceInfoReplicationIntervalSeconds
    //DiscoveryClient -> statusChangeListener.notify ile server'a gider(appInfoManager.setStatus'ler bununla gider)
    //DiscoveryClient.renew ile eurekaServer'a heartbeat'ler yollanir(30 sn de bir giden)(sadece statu gidiyor UP/DOWN vs)   --> leaseRenewalIntervalInSeconds
    //Never send status by outOfService
    //https://github.com/spring-cloud/spring-cloud-netflix/issues/3941
}