package com.example.producer.kafka.dlt;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface ProducerDltHandler {
    void persistDltFailure(ConsumerRecord<?, ?> record, Exception exception, int retryCount, String targetTopic);
}
