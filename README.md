# About

Repo meant to reproduce bug with application insights context/span leakage when using apache camel, spring boot,
application-insights javaagent and chained jms inout exchanges. It contains an example application, meant to be run as
two separate processes using two different spring profiles.

Apache camel route that demonstrate the issue are located in ReproducerRoute class.

# Steps to reproduce

1. Download application insights 3.7.10 agent and put it in the root folder
2. Start up activemq classic broker. Can be done with docker using
   docker run --name activemq -p 61616:61616 -p 8161:8161 apache/activemq-classic
3. Open first terminal and run
   SPRING_PROFILES_ACTIVE=responder ./gradlew bootRun
   This will run the example app in responder mode, meant to just send replies on the reply queue
4. Open the second terminal and run
   APPLICATIONINSIGHTS_CONNECTION_STRING='InstrumentationKey=00000000-0000-0000-0000-000000000000' \
   SPRING_PROFILES_ACTIVE=reproducer \
   ./gradlew bootRun \
   -PappJvmArgs="-javaagent:$(pwd)/applicationinsights-agent-3.7.10.jar"
   This will run the app meant to demonstrate the bug

In the second terminal you will see relevant logs, that show that trace and span IDs get stuck in QueueReplyManager
threads between invocations. Both firstInvocation and secondInvocation for step-2-logger will have the same trace and
span IDs. Trace and span IDs are included in every log entry via logback-spring.xml.