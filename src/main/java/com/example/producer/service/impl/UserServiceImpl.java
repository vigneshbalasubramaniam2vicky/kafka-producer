package com.example.producer.service.impl;

import com.example.producer.dto.UserRegistrationRequest;
import com.example.producer.dto.UserRegistrationResponse;
import com.example.producer.entity.User;
import com.example.producer.exception.UserRegistrationException;
import com.example.producer.kafka.producer.UserProducerService;
import com.example.producer.repository.UserRepository;
import com.example.producer.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserProducerService userProducerService;

    @Override
    public UserRegistrationResponse registerUser(UserRegistrationRequest request) {
        try {
            LocalDateTime now = LocalDateTime.now();
            User user = User.builder()
                    .name(request.getName())
                    .email(request.getEmail())
                    .phoneNumber(request.getPhoneNumber())
                    .createdDt(now)
                    .updatedDt(now)
                    .build();

            User savedUser = userRepository.save(user);
            log.info("User saved to MongoDB with id: {}", savedUser.getId());

            userProducerService.publishUserRegisteredEvent(savedUser);
            log.info("User registration event published for user id: {}", savedUser.getId());

            return UserRegistrationResponse.builder()
                    .id(savedUser.getId())
                    .name(savedUser.getName())
                    .email(savedUser.getEmail())
                    .phoneNumber(savedUser.getPhoneNumber())
                    .createdDt(savedUser.getCreatedDt())
                    .status("SUCCESS")
                    .message("User registered and event published successfully")
                    .build();
        } catch (Exception ex) {
            log.error("Error during user registration", ex);
            throw new UserRegistrationException("Failed to register user", ex);
        }
    }
}
