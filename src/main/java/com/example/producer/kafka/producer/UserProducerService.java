package com.example.producer.kafka.producer;

import com.example.producer.entity.User;

public interface UserProducerService {
    void publishUserRegisteredEvent(User user);
}
