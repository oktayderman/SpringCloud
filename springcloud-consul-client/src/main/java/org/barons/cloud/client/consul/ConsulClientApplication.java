package org.barons.cloud.client.consul;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jakarta.annotation.PostConstruct;

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

	public static void main(String[] args) {
		SpringApplication.run(ConsulClientApplication.class, args);
	}
}