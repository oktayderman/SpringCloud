package org.barons.cloud.client.consul;

import com.ecwid.consul.v1.ConsulClient;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

//@Component
public class ConsulShutdownListener implements ApplicationListener<ContextClosedEvent> {

    private final ConsulClient consulClient;

    public ConsulShutdownListener(ConsulClient consulClient) {
        this.consulClient = consulClient;
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        try {
            consulClient.agentCheckFail("service:aom-client");
            System.out.println("Consul’a shutdown FAIL bildirildi (event).");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
