package org.barons.cloud.client.consul;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.OperationException;
import com.ecwid.consul.v1.agent.model.NewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.CompositeHealth;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.consul.discovery.ConsulDiscoveryProperties;
import org.springframework.cloud.consul.discovery.HeartbeatProperties;
import org.springframework.cloud.consul.discovery.ReregistrationPredicate;
import org.springframework.cloud.consul.discovery.TtlScheduler;
import org.springframework.cloud.consul.serviceregistry.ApplicationStatusProvider;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;

import static org.springframework.boot.actuate.health.Status.UP;

@Component
@ConditionalOnProperty("spring.cloud.consul.discovery.heartbeat.enabled")
public class MyTTLScheduler extends TtlScheduler {
    //https://chatgpt.com/c/6839fda5-ee8c-8012-bfb0-fc461800c0bd
    //https://github.com/spring-cloud/spring-cloud-consul/issues/676
    /**
     * Bu sınıfı herhalde custom olarak health check'i biz gönderelim consul bize gelmesi, biz düşer düşmez de hemen fail atabilelim diye yapmışız
     *  ConsulClient.agentCheckFail(this.checkId) özellikle şöyle düşer düşmez atabiliriz
     */
   static Logger log = LoggerFactory.getLogger(MyTTLScheduler.class);
    private final TaskScheduler scheduler = new ConcurrentTaskScheduler(Executors.newSingleThreadScheduledExecutor());
    private final Map<String, ScheduledFuture> serviceHeartbeats = new ConcurrentHashMap<>();
    @Autowired
    HealthEndpoint healthEndpoint;
    private ReregistrationPredicate reregistrationPredicate;
    private  final ConsulClient client;
    private final HeartbeatProperties heartbeatProperties;
    private final Map<String, NewService> registeredServices = new ConcurrentHashMap<>();
    private final ConsulDiscoveryProperties discoveryProperties;

    public MyTTLScheduler(HeartbeatProperties heartbeatProperties, ConsulDiscoveryProperties discoveryProperties, ConsulClient client, @Autowired ReregistrationPredicate reregistrationPredicate, ObjectProvider<ApplicationStatusProvider> applicationStatusProviderFactory) {
        super(heartbeatProperties, discoveryProperties, client, reregistrationPredicate,applicationStatusProviderFactory);
        this.client = client;
        this.heartbeatProperties = heartbeatProperties;
        this.reregistrationPredicate = reregistrationPredicate;
        this.discoveryProperties = discoveryProperties;
    }

    public void add(final NewService service) {
        add(service.getId());
        this.registeredServices.put(service.getId(), service);
    }


    public void remove(String instanceId) {
        ScheduledFuture task = this.serviceHeartbeats.get(instanceId);
        if (task != null) {
            task.cancel(true);
        }
        this.serviceHeartbeats.remove(instanceId);
        this.registeredServices.remove(instanceId);
    }


    public void add(String instanceId) {
        ScheduledFuture task = this.scheduler.scheduleAtFixedRate(new Task(instanceId, this),8000);
        ScheduledFuture previousTask = this.serviceHeartbeats.put(instanceId, task);
        if (previousTask != null) {
            previousTask.cancel(true);
        }
    }

    static class Task implements Runnable {
        MyTTLScheduler ttlScheduler;
        String serviceId;
        String checkId;

        Task(String serviceId, MyTTLScheduler ttlScheduler) {
            this.serviceId = serviceId;
            if (!this.serviceId.startsWith("service:")) {
                this.checkId = "service:" + this.serviceId;
            }
            else {
                this.checkId = this.serviceId;
            }
            this.ttlScheduler = ttlScheduler;
        }

            @Override
            public void run() {
                try {
                    //todo some of healthIndicators have to excluded!
                    CompositeHealth healthComponent = (CompositeHealth) this.ttlScheduler.healthEndpoint.health();
                    if(healthComponent.getStatus() == UP) {
                        this.ttlScheduler.client.agentCheckPass(this.checkId);
                    }else{//TODO bunu tam shutdown aninda yaparak hemen bildirim yapabiliriz.
                        this.ttlScheduler.client.agentCheckFail(this.checkId);
                    }
                }
                catch (OperationException e) {
                    if (this.ttlScheduler.heartbeatProperties.isReregisterServiceOnFailure()
                            && this.ttlScheduler.reregistrationPredicate.isEligible(e)) {
                        log.warn(e.getMessage());
                        NewService registeredService = this.ttlScheduler.registeredServices.get(this.serviceId);
                        if (registeredService != null) {
                            if (log.isInfoEnabled()) {
                                log.info("Re-register " + registeredService);
                            }
                            this.ttlScheduler.client.agentServiceRegister(registeredService,
                                    this.ttlScheduler.discoveryProperties.getAclToken());
                        }
                        else {
                            log.warn("The service to re-register is not found.");
                        }
                    }
                    else {
                        throw e;
                    }
                }
            }

    }

}
