package org.barons.cloud.client.eureka;


import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.eureka.EurekaHealthCheckHandler;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.boot.actuate.health.Status.UP;

@Component
@ConditionalOnProperty("eureka.client.healthcheck.enabled")
public class MyHealthCheckHandler extends EurekaHealthCheckHandler {
    //  when state is 'STARTING' EurekaHealthCheckHandler sends the status as "UP"! so this class has been written to fix this
    Logger logger = LoggerFactory.getLogger(MyHealthCheckHandler.class);
    SimpleStatusAggregator simpleStatusAggregator = new SimpleStatusAggregator();
    @Autowired
    @Lazy
    private EurekaClient eurekaClient;
    @Autowired
    @Lazy
    private DiscoveryClient discoveryClient;
    @Autowired
    HealthEndpoint healthEndpoint;
    Map<String, HealthIndicator> healthIndicatorList = null;

    //ReactiveHealthIndicator,
    // https://stackoverflow.com/questions/44849568/how-to-add-a-custom-health-check-in-spring-boot-health
    //https://www.baeldung.com/spring-boot-actuators

    @Autowired
    ApplicationInfoManager appInfoManager;

    public MyHealthCheckHandler(StatusAggregator statusAggregator) {
        super(statusAggregator);
    }


    void getIndicators() {
        try {
            Field healthContributors = this.getClass().getSuperclass().getDeclaredField("healthContributors");
            healthContributors.setAccessible(true);
            healthIndicatorList = (Map<String, HealthIndicator>) healthContributors.get(this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Attention status here will override appInfoManager.status in {@link com.netflix.discovery.DiscoveryClient#refreshInstanceInfo}
     * So another thread if set appInfoManagerStatus, we must return same status here
     *
     * @return
     */
    //eureka.client.healthcheck.enabled=true when enabled, this client will send the status according to this function's response(aggregated statuses of  healthIndicators list)
    protected InstanceInfo.InstanceStatus getHealthStatus() {
        if (healthIndicatorList == null)
            getIndicators();
        //  org.springframework.cloud.netflix.eureka.DiscoveryHealthIndicator should not contain in aggregateStatus and euroka is already excluding this! otherwise down cycle may happen
        // this is the eureka server healthIndicator(remote)
        Status aggregatedStatus = getStatus(simpleStatusAggregator);
        InstanceInfo.InstanceStatus status = appInfoManager.getInfo().getStatus();
        if (UP.equals(aggregatedStatus)) {
            if (status != InstanceInfo.InstanceStatus.UP) {
                logger.info("HealthIndicators reported {} setting status to UP from {}", aggregatedStatus, status);
            }
            return InstanceInfo.InstanceStatus.UP;
        } else {
            if (status != InstanceInfo.InstanceStatus.DOWN) {
                logger.info("HealthIndicators reported {} setting status to DOWN from {} non-UP-healthIndicators:{}",
                        aggregatedStatus, status, healthIndicatorList.entrySet().stream().map(a -> new Pair<>(a.getKey(), a.getValue().health().getStatus()))
                                .filter(a -> !UP.equals(a.second())).map(a -> a.first() + "->" + a.second()).collect(Collectors.joining(",")));

            }
            return InstanceInfo.InstanceStatus.DOWN;
        }
    }
}

