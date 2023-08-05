package org.barons;

import brave.Tracing;
import brave.propagation.B3Propagation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Bean
    public Tracing braveTracing() {
        return Tracing.newBuilder()
                .propagationFactory(B3Propagation.newFactoryBuilder().injectFormat(B3Propagation.Format.MULTI).build())
                .build();
    }
    @Autowired
    private GreetingClient greetingClient;
    @RequestMapping("/get-greeting")
    public String greeting(Model model) {
        logger.info("I am here with traceId");
        model.addAttribute("greeting", greetingClient.greeting());
        return "greeting-view";
    }
}
