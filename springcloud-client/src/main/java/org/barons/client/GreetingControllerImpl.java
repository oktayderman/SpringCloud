package org.barons.client;

import brave.Tracer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

/**
 * User: Oktay CEKMEZ
 * Date: 7.06.2022
 * Time: 21:20
 */
@RestController
public class GreetingControllerImpl implements GreetingController{
    Logger logger = LoggerFactory.getLogger(GreetingControllerEurokaClient.class);

    @Autowired
    private Tracer tracer;



    @Override
    public String greeting(@RequestHeader MultiValueMap<String, String> headers) {
        tracer.currentSpan().tag("heyMicrometer", "true");

        logger.info("headers:\n" + headers.toString());
        return String.format(
                "Hello from"); //eurekaClient.getApplication(appName).getName());
    }
}