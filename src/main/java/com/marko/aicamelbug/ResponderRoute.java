package com.marko.aicamelbug;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("responder")
public class ResponderRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("activemqComponent:queue:request_queue")
                .setBody(body());
    }
}
