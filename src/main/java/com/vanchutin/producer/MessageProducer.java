package com.vanchutin.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.vanchutin.event.Event;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class MessageProducer implements CommandLineRunner {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    EventGenerator eventGenerator;

    public void run(String... args) throws Exception {
        while(true) {
            Event newEvent = eventGenerator.generate();
            String message = objectMapper.writeValueAsString(newEvent);
            log.info(String.format("Sending message...%s", message));
            rabbitTemplate.convertAndSend("eventExchange", "atm.event", message);
            Thread.sleep(1000);
        }
    }

}
