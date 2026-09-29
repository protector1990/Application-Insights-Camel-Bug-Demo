package com.marko.aicamelbug;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.context.Context;
import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("reproducer")
public class ReproducerRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("direct:step1")
                .to(ExchangePattern.InOut, "activemqComponent:queue:request_queue?useMessageIDAsCorrelationID=false&requestTimeout=6000&replyToType=Shared&replyTo=response_queue&replyToConcurrentConsumers=1")
                .to("log:step-1-logger")
                .to(ExchangePattern.InOnly, "direct:step2");

        from("direct:step2") // Changing route type to seda seems to mitigate the issue
                .to(ExchangePattern.InOut, "activemqComponent:queue:request_queue?useMessageIDAsCorrelationID=false&requestTimeout=6000&replyToType=Shared&replyTo=response_queue&replyToConcurrentConsumers=1")
                // Workaround. Creates a new span and makes it current. Without it, the span and trace ids remain
                // the same per QueueReplyManager thread indefintely
                //.process(exchange -> {
                //    var tracer = GlobalOpenTelemetry.getTracer("test");
                //    Span newSpan = tracer.spanBuilder("reset")
                //            .setNoParent()
                //            .startSpan();
                //    newSpan.makeCurrent();
                //})
                .to("log:step-2-logger");
    }
}
