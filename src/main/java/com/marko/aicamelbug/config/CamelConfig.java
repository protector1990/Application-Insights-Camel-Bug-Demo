package com.marko.aicamelbug.config;

import jakarta.jms.ConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.component.jms.JmsComponent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CamelConfig {

    @Bean
    public JmsComponent activemqComponent(ConnectionFactory connectionFactory, final CamelContext camelContext) {
        final JmsComponent component = JmsComponent.jmsComponent(connectionFactory);
        camelContext.addComponent("activemqComponent", component);
        return component;
    }
}
