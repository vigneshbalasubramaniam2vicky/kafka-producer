package com.example.producer.kafka.producer;

import com.example.producer.config.TopicProperties;
import com.example.producer.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserProducerServiceImpl implements UserProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final TopicProperties topicProperties;
    private final RetryTemplate retryTemplate;
    private final DeadLetterPublishingRecoverer deadLetterPublishingRecoverer;

    @Override
    public void publishUserRegisteredEvent(User user) {
        String key = user.getId();

        try {
            retryTemplate.execute(context -> {
                int attempt = context.getRetryCount() + 1;
                log.info("Publishing message to topic {}. Attempt {}/{}", topicProperties.getMainTopic(), attempt, topicProperties.getRetryAttempts());
                kafkaTemplate.send(topicProperties.getMainTopic(), key, user).get(10, TimeUnit.SECONDS);
                log.info("Successfully published user event for id {}", user.getId());
                return null;
            }, context -> {
                Exception exception = new RuntimeException(context.getLastThrowable());
                log.error("All retry attempts exhausted. Sending message to DLT {}", topicProperties.getDltTopic(), exception);
                ConsumerRecord<Object, Object> failedRecord = new ConsumerRecord<>(topicProperties.getMainTopic(), 0, 0L, key, user);
                deadLetterPublishingRecoverer.accept(failedRecord, exception);
                throw exception;
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
