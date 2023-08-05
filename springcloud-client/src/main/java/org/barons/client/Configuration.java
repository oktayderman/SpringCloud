package org.barons.client;

import brave.Tracer;
import brave.handler.MutableSpan;
import brave.handler.SpanHandler;
import brave.propagation.TraceContext;
import org.slf4j.Logger;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class Configuration {
    //see BraveAutoConfiguration
    @Bean(name = {"logSpanHandler"})
    protected SpanHandler getLogSpanHandler() {
        return new SpanHandler() {
            private final Logger logger = org.slf4j.LoggerFactory.getLogger(Tracer.class);

            @Override
            public boolean end(TraceContext context, MutableSpan span, Cause cause) {
                logger.info(span.toString());
                return true;
            }
        };
    }
}
