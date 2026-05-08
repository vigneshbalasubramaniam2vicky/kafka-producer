package com.example.producer.kafka.config;

import com.example.producer.config.TopicProperties;
import com.example.producer.kafka.dlt.ProducerDltHandler;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

@Configuration
@RequiredArgsConstructor
public class RetryConfig {

    private final TopicProperties topicProperties;

    @Bean
    public RetryTemplate retryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();

        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy(topicProperties.getRetryAttempts());
        FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();
        backOffPolicy.setBackOffPeriod(topicProperties.getRetryIntervalMs());

        retryTemplate.setRetryPolicy(retryPolicy);
        retryTemplate.setBackOffPolicy(backOffPolicy);
        return retryTemplate;
    }

    @Bean
    public DeadLetterPublishingRecoverer deadLetterPublishingRecoverer(
            KafkaOperations<String, Object> kafkaOperations,
            ProducerDltHandler producerDltHandler) {

        return new DeadLetterPublishingRecoverer(kafkaOperations,
                (record, ex) -> new TopicPartition(topicProperties.getDltTopic(), 0)) {
            @Override
            public void accept(ConsumerRecord<?, ?> record, Exception exception) {
                super.accept(record, exception);
                producerDltHandler.persistDltFailure(record, exception, topicProperties.getRetryAttempts(), topicProperties.getDltTopic());
            }
        };
    }
}
