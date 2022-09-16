package org.barons.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * User: Oktay CEKMEZ
 * Date: ${DATE}
 * Time: ${TIME}
 */
@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApplication {
    //AbstractInstanceRegistry -> Running the evict task with starting too late! so cannot evict dead client at first couple of minutes
    public static void main(String[] args) {
        SpringApplication.run(EurekaServerApplication.class, args);



        //Client Configuration
        //eureka.instance.leaseRenewalIntervalInSeconds (default 30 seconds) -> client -> renew lease in eureka server interval(hearthbeat gonderme gibi)

        //Eureka Server
        //Renews (last min): represents how many renews received from Eureka instance in last minute
        //Renews threshold: the renews that Eureka server expects received from Eureka instance per minute.
        //2 euroka client -> 2*2=4 renews per minute + 1 ->> threshold 5
        //Eureka server makes an implicit assumption that the clients are sending their heartbeat at a fixed rate of 1 every 30s.
        //when leaseRenewalIntervalInSeconds is different than 30 seconds it does not care, so before changing this we can change renewalPercentThreshold
        //eureka.instance.registry.expectedNumberOfRenewsPerMin is the "+1" at the upper line
        //found threshold will be multiplied with this factor to find out "applied" threshold in cluster
        //eureka.server.renewalPercentThreshold= 0.85 -> 5 * 0.85 -> 4.25 -> 5
        //SELF PRESERVATION MODE: if Renews (last min) is less than Renews threshold(5),
        // self preservation mode will be activated and instance will not be removed until heartbeats count per minute is over threshold*** bad
        //renews may be increased by decreasing the leaseRenewalIntervalInSeconds to 5 seconds. (threshold wont change)
        //In production environment, generally we deploy two Eureka server and registerWithEureka will be set to true.
        // So the threshold will be 2(without any client), and Eureka server will renew lease to itself twice/minute(4 times), so RENEWALS ARE LESSER THAN THRESHOLD won't be a problem.
        //eureka.instance.leaseExpirationDurationInSeconds if server does not receive hearthbeat within this time, instance will be removed
        //but will retry continue max interval is ->> eureka.instance.leaseRenewalIntervalInSeconds * eureka.client.heartbeatExecutorExponentialBackOffBound


        //Eureka Server configuration
        //registerWithEureka should be true at production.
        //Eureka peers don’t count towards "Renews threshold", but the heartbeats from them is considered within the number of renewals received last minute

        //different than heartbeat, all clients send their instanceInfo by this interval
        //eureka.client.instanceInfoReplicationIntervalSeconds

        //https://stackoverflow.com/questions/33921557/understanding-spring-cloud-eureka-server-self-preservation-and-renew-threshold
        //https://blogs.asarkar.com/technical/netflix-eureka/

        //Turn off self preservation
        //https://dzone.com/articles/the-mystery-of-eurekas-self-preservation
        //https://stackoverflow.com/questions/33150981/eurekas-self-preservation-mode-never-recovers
    }
}