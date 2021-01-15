package com.example.study.kafka.service;

import com.example.study.kafka.model.Event;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class ConsumerService {

    @Autowired
    private RedisTemplate<String, Event> redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @KafkaListener(topics = "spring-topic-test", groupId = "user_test", containerFactory = "kafkaContainerFactory")
    public void consumer(String eventStringJson) throws JsonProcessingException {
        log.info("Consumer event object in kafka "  + eventStringJson);
        Event event = objectMapper.readValue(eventStringJson, Event.class);
        log.info("Convert string to object " + event.toString());
        redisTemplate.opsForHash().put(event.getKey(), event.getKey().hashCode(), event);
    }

}
