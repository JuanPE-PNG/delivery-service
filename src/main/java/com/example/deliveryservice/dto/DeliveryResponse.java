package com.example.deliveryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryResponse {
    private Integer deliveryId;
    private Integer paymentId;
    private Integer userId;
    private String customerAddress;
    private String customerCity;
    private String deliveryStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer assignedDriverId;
    private String message;
} 