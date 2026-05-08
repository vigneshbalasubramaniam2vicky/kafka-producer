package com.example.producer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationResponse {
    private String id;
    private String name;
    private String email;
    private String phoneNumber;
    private LocalDateTime createdDt;
    private String status;
    private String message;
}
