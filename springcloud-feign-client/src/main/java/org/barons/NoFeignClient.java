package org.barons;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * User: Oktay CEKMEZ
 * Date: 16.06.2022
 * Time: 20:30
 */
@RestController
public class NoFeignClient {
    @Autowired
    private EurekaClient eurekaClient;

    @RequestMapping("/get-greeting-no-feign")
    public String greeting(Model model) {

        InstanceInfo service = eurekaClient
                .getApplication("spring-cloud-eureka-client")
                .getInstances()
                .get(0);

        String hostName = service.getHostName();
        int port = service.getPort();
        //With this, we could do a standard request with any http client:
        return "heyy";

    }
}
