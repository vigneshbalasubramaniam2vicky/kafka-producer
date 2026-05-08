package com.example.producer.repository;

import com.example.producer.entity.ProducerDltLog;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProducerDltLogRepository extends MongoRepository<ProducerDltLog, String> {
}
