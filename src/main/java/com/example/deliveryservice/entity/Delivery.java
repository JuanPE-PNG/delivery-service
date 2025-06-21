package com.example.deliveryservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "delivery_tb")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Delivery {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer deliveryId;
    
    private Integer paymentId;
    private Integer userId;
    private String customerAddress;
    private String customerCity;
    private String deliveryStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer assignedDriverId;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (deliveryStatus == null) {
            deliveryStatus = "PENDIENTE";
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
} 