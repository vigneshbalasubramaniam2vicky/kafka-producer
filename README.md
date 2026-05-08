# kafka-producer-service

Production-ready Spring Boot producer microservice using Java 21, Kafka, and MongoDB.

## Features
- Register users via REST endpoint `POST /api/users/register`
- Persist user into MongoDB collection `user`
- Publish persisted user as JSON to Kafka topic `demo-topic`
- Producer-side retry with `RetryTemplate` (10 attempts, 5s interval)
- Dead-letter handling with `DeadLetterPublishingRecoverer`
- Failed publish logs persisted into `producer_dlt_log` MongoDB collection
- Validation, layered architecture, and global exception handling

## API
### Register User
`POST /api/users/register`

Request:
```json
{
  "name": "Vignesh",
  "email": "vignesh@gmail.com",
  "phoneNumber": "9876543210"
}
```

## Kafka Commands
```bash
# 1. Start Zookeeper
bin/zookeeper-server-start.sh config/zookeeper.properties

# 2. Start Kafka broker
bin/kafka-server-start.sh config/server.properties

# 3. Create main topic
bin/kafka-topics.sh --create --topic demo-topic --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1

# 4. Create DLT topic
bin/kafka-topics.sh --create --topic demo-topic-dlt --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1

# 5. Run the producer service
mvn spring-boot:run

# 6. Test API
curl --location 'http://localhost:8080/api/users/register' \
--header 'Content-Type: application/json' \
--data-raw '{
  "name": "Vignesh",
  "email": "vignesh@gmail.com",
  "phoneNumber": "9876543210"
}'
```
