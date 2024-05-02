package com.turkcell.CustomerService.kafka.producer;

import com.turkcell.commonpackage.events.customer.CreatedCustomerEvent;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;


import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;

import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomerProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerProducer.class);

    private NewTopic topic;

    private final KafkaTemplate<String, CreatedCustomerEvent> kafkaTemplate;

    public void sendMessage(CreatedCustomerEvent event) {
        LOGGER.info(String.format("Customer added =>%s", event.toString()));

        Message<CreatedCustomerEvent> message = MessageBuilder
                .withPayload(event)
                .setHeader(KafkaHeaders.TOPIC,topic.name())
                .build();

        kafkaTemplate.send(message);
    }
}