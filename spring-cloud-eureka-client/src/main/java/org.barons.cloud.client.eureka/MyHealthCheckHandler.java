package org.barons.cloud.client.eureka;


import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.eureka.EurekaHealthCheckHandler;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.boot.actuate.health.Status.UP;

@Component
@ConditionalOnProperty("eureka.client.healthcheck.enabled")
public class MyHealthCheckHandler extends EurekaHealthCheckHandler {
	Logger logger = LoggerFactory.getLogger(MyHealthCheckHandler.class);

	@Autowired
	@Lazy
	private EurekaClient eurekaClient;
	@Autowired
	HealthEndpoint healthEndpoint;

	//ReactiveHealthIndicator,
	// https://stackoverflow.com/questions/44849568/how-to-add-a-custom-health-check-in-spring-boot-health
	//https://www.baeldung.com/spring-boot-actuators

	@Autowired
	ApplicationInfoManager appInfoManager;
	@Autowired
	private DiscoveryClient discoveryClient;

	public MyHealthCheckHandler(StatusAggregator statusAggregator) {
		super(statusAggregator);
	}

	/**
	 * Attention status here will override appInfoManager.status in {@link com.netflix.discovery.DiscoveryClient#refreshInstanceInfo}
	 * So another thread if set appInfoManagerStatus, we must return same status here
	 * @return
	 */

	protected InstanceInfo.InstanceStatus getHealthStatus() {
		CompositeHealth healthComponent = (CompositeHealth) healthEndpoint.health();

		InstanceInfo.InstanceStatus status = appInfoManager.getInfo().getStatus();
		if (healthComponent.getStatus() == UP) {
			if(status != InstanceInfo.InstanceStatus.UP){
				logger.info("healthEndpoint reported {} setting status UP from {}",healthComponent.getStatus(),status);
			}
			return InstanceInfo.InstanceStatus.UP;
		} else {
			if(status == InstanceInfo.InstanceStatus.UP){
				logger.info("healthEndpoint reported {} setting status DOWN from {} non-UP-components: '{}'",healthComponent.getStatus(),
						status, healthComponent.getComponents().entrySet().stream().filter(a->a.getValue().getStatus() != UP).map(Map.Entry::getKey).collect(Collectors.joining(", ")));
			}
			return InstanceInfo.InstanceStatus.DOWN;
		}
	}

}
	//when state is starting event we return "starting" here, it becomes "UP"! do not use healthcheck with eureka     healthcheck.enabled: false
	//one of EurekaHealthCheckHandler healthContributors saying we are UP and so status is being UP, customHandler may be used...
	//eureka.client.healthcheck.enabled=true when enabled, eureka server will get health info from clients and health statuses of the apps may be questioned from directly eureka server
	//and if status down here eureka server will see this

