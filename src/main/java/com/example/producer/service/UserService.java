package com.example.producer.service;

import com.example.producer.dto.UserRegistrationRequest;
import com.example.producer.dto.UserRegistrationResponse;

public interface UserService {
    UserRegistrationResponse registerUser(UserRegistrationRequest request);
}
