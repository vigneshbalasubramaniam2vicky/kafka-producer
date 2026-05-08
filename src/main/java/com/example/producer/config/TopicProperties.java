package com.example.producer.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "app.kafka")
public class TopicProperties {
    private String mainTopic;
    private String dltTopic;
    private Integer retryAttempts;
    private Long retryIntervalMs;
}
