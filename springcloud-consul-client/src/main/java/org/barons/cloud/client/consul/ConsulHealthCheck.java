package org.barons.cloud.client.consul;



import com.ecwid.consul.v1.ConsulClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.consul.discovery.TtlScheduler;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class ConsulHealthCheck implements HealthIndicator {
	//ReactiveHealthIndicator,
	// https://stackoverflow.com/questions/44849568/how-to-add-a-custom-health-check-in-spring-boot-health
	//https://www.baeldung.com/spring-boot-actuators


	@Autowired
	private DiscoveryClient discoveryClient;
	@Autowired
	ConsulClient consulClient;

	@Override
	public Health health() {
		System.out.println("services:" + (discoveryClient.getInstances("aom-client")));
			return Health.up().build();
		}
	}
