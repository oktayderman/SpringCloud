package org.barons;

import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class AnnotationDrivenEventListener {
    @PostConstruct
    void init() {
        System.out.println("inited");
    }

    @Async
    @EventListener //synch to make it asynch mark as asynch
    public void handleContextStart(ContextRefreshedEvent cse) {
        System.out.println("started");
    }
}