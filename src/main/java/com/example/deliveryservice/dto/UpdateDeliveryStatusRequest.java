package com.example.deliveryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateDeliveryStatusRequest {
    private Integer deliveryId;
    private String newStatus;
} 