package com.marko.aicamelbug;

import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("reproducer")
public class Runner implements CommandLineRunner {

    @Autowired
    private ProducerTemplate producerTemplate;

    @Override
    public void run(String... args) throws Exception {
        producerTemplate.sendBody("direct:step1", "firstInvocation");
        producerTemplate.sendBody("direct:step1", "secondInvocation");
    }
}
