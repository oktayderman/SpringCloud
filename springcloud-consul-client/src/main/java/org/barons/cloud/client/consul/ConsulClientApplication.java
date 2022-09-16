package org.barons.cloud.client.consul;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

/**
 * User: Oktay CEKMEZ
 * Date: ${DATE}
 * Time: ${TIME}
 */
@SpringBootApplication
public class ConsulClientApplication {

	static ConsulClientApplication instance;


	ConsulClientApplication() {
		ConsulClientApplication.instance = this;
	}

	@PostConstruct
	public void initialize() {
		new Thread(() -> {
			while (true) {
				try {
					setStatus();
				} finally {
					sleep(10);
				}
			}
		}).start();
	}

	public static void main(String[] args) {
		SpringApplication.run(ConsulClientApplication.class, args);
	}

	public void setStatus() {
		//todoProd hearthbeat gonderemiyorken(kill olurken veya suspend'de) euroka ne yapiyor 10sn icinde anlamali
		//healthCheck bir ise yariyor mu

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