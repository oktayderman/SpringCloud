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
import org.springframework.context.annotation.Lazy;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * User: Oktay CEKMEZ
 * Date: ${DATE}
 * Time: ${TIME}
 */
@SpringBootApplication
public class EurekaClientApplication {

	static EurekaClientApplication	instance;
	@Autowired
	ApplicationInfoManager			appInfoManager;

	EurekaClientApplication() {
		EurekaClientApplication.instance = this;
	}


	public static void main(String[] args) {
		SpringApplication.run(EurekaClientApplication.class, args);
	}

	public void setStatus() {
		//todoProd hearthbeat gonderemiyorken(kill olurken veya suspend'de) euroka ne yapiyor 10sn icinde anlamali
		/*Application application = eurekaClient.getApplication("aom-client");
		if (application != null) {
			List<InstanceInfo> instances = application.getInstances();
			System.out.println("instanceCount:" + instances.size());
			instances.forEach(System.out::println);
		}*/
	}

	public static void sleep(int seconds) {
		try {
			Thread.sleep(seconds * 1000L);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
	//InstanceInfoReplicator.run ile degisen statü varsa yeni statü eureka server'a yollanir(instanceInfo)  --> instanceInfoReplicationIntervalSeconds
	//DiscoveryClient -> statusChangeListener.notify ile server'a gider(appInfoManager.setStatus'ler bununla gider)
	//DiscoveryClient.renew ile eurekaServer'a heartbeat'ler yollanir(30 sn de bir giden)(sadece statu gidiyor UP/DOWN vs)   --> leaseRenewalIntervalInSeconds
	//Never send status by outOfService
	//https://github.com/spring-cloud/spring-cloud-netflix/issues/3941
	//Eureka servers never send keep-alive requests to their registered clients (as opposed to some Traffic Mangers), instead, Eureka clients send heartbeats to Eureka servers.
}