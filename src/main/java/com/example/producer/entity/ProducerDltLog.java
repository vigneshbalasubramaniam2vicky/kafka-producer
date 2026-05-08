package com.example.producer.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "producer_dlt_log")
public class ProducerDltLog {

    @Id
    private String id;
    private String failedMessage;
    private String errorMessage;
    private LocalDateTime failedAt;
    private Integer retryCount;
    private String targetTopic;
}
