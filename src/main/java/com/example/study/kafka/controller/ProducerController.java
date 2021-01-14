package com.example.study.kafka.controller;

import com.example.study.kafka.model.Event;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.protocol.Message;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController
@Slf4j
public class ProducerController {

    @Autowired
    private KafkaTemplate<String, Event> kafkaTemplate;

    @Autowired
    private NewTopic springTopic;

    @PostMapping("/producer")
    public String createEvent(@RequestBody Event event) throws ExecutionException, InterruptedException, JsonProcessingException {
        log.info("INFO Event: " + event.toString());
        return kafkaTemplate.send(springTopic.name(), event).get().toString();
    }

    @PostMapping("/producer/record")
    public String createEventRecord(@RequestBody Event event) throws ExecutionException, InterruptedException, JsonProcessingException {
        log.info("INFO Post send message wrapped record: " + event.toString());
        ProducerRecord<String, Event> producerRecordEvent =
                new ProducerRecord(springTopic.name(), event.getKey(), event.getValue());

        return kafkaTemplate.send(producerRecordEvent).get().toString();
    }

}
