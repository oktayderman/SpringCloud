package org.barons.cloud.client.eureka;


import com.netflix.appinfo.ApplicationInfoManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import static com.netflix.appinfo.InstanceInfo.InstanceStatus.DOWN;
import static com.netflix.appinfo.InstanceInfo.InstanceStatus.UP;

@Component
public class ModuleHealthCheck implements HealthIndicator {
    Logger logger = LoggerFactory.getLogger(ModuleHealthCheck.class);
    //ReactiveHealthIndicator,
    // https://stackoverflow.com/questions/44849568/how-to-add-a-custom-health-check-in-spring-boot-health
    //https://www.baeldung.com/spring-boot-actuators

    @Autowired
    private DiscoveryClient discoveryClient;
    @Autowired
    private ApplicationInfoManager applicationInfoManager;
    volatile Health health = Health.down().build();

    @PostConstruct
    public void start() {
        new Thread(() -> {
            Thread.currentThread().setName("ModuleHealthCheck");
            while (true) {
                try {
                    resolveHealth();
                    if(applicationInfoManager.getInfo().getStatus() == UP && health.getStatus() == Status.DOWN){
                        logger.warn("ModuleHealth reported a problem setting status to DOWN immediately");
                        applicationInfoManager.setInstanceStatus(DOWN);
                    }
                } finally {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                }
            }

        }).start();
    }

    private void resolveHealth() {
        health = Health.up().build();
    }

    @Override
    public Health health() {
        return health;
    }
}
//when state is starting event we return starting here, it becomes "UP"! do not use healthcheck with eureka     healthcheck.enabled: false
//one of EurekaHealthCheckHandler healthContributors saying we are UP and so status is being UP, customHandler may be used...
//eureka.client.healthcheck.enabled=true when enabled, eureka server will get health info from clients, health statuses of the apps may be questioned from directly eureka server
//and if status down here eureka server will see this

