package com.example.producer.kafka.dlt;

import com.example.producer.entity.ProducerDltLog;
import com.example.producer.repository.ProducerDltLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProducerDltHandlerImpl implements ProducerDltHandler {

    private final ProducerDltLogRepository producerDltLogRepository;

    @Override
    public void persistDltFailure(ConsumerRecord<?, ?> record, Exception exception, int retryCount, String targetTopic) {
        ProducerDltLog dltLog = ProducerDltLog.builder()
                .failedMessage(record.value() != null ? record.value().toString() : "null")
                .errorMessage(exception.getMessage())
                .failedAt(LocalDateTime.now())
                .retryCount(retryCount)
                .targetTopic(targetTopic)
                .build();

        producerDltLogRepository.save(dltLog);
        log.error("Message moved to DLT topic {} and persisted to MongoDB", targetTopic, exception);
    }
}
