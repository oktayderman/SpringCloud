package org.barons;

import brave.Tracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {
    Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    Tracer tracer;
    @Autowired
    private GreetingClient greetingClient;
    @RequestMapping("/get-greeting")
    public String greeting(Model model) {
        logger.info("I am here with traceId");
        model.addAttribute("greeting", greetingClient.greeting());
        return "greeting-view";
    }
}
