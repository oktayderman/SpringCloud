package org.barons;

import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class AnnotationDrivenEventListener {
    @PostConstruct
    void init() {
        System.out.println("inited");
    }

    @Async//without that publisher thread will call this method synch
    @EventListener
    @Order(1)//sets order to call amoung ContextRefreshedEvent listeners
    public void handleContextStart(ContextRefreshedEvent cse) {
        System.out.println("started");
    }
}